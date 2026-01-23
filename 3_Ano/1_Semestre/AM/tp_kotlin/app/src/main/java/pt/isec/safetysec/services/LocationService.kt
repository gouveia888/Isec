package pt.isec.safetysec.services

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.google.firebase.firestore.FirebaseFirestore
import pt.isec.safetysec.R
import android.location.Location
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat.checkSelfPermission
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

class LocationService(private val context: Context) {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    fun getCurrentLocation(onResult: (String) -> Unit) {
        if (checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            onResult(context.getString(R.string.no_premission_gps))
            return
        }

        fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            null
        ).addOnSuccessListener { location ->
            if (location != null) {
                onResult("${location.latitude}, ${location.longitude}")
            } else {
                //se nao for possivel obter a loc atual apresenta a ultima registada
                fusedLocationClient.lastLocation.addOnSuccessListener { last ->
                    if (last != null) {
                        onResult("${last.latitude}, ${last.longitude}")
                    } else {
                        onResult(context.getString(R.string.obtain_signal_gps))
                    }
                }
            }
        }.addOnFailureListener {
            onResult(context.getString(R.string.obtain_signal_gps))
        }
    }
}

class LocationForegroundService : Service() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var lastLocation: Location? = null
    private var lastMovementTimestamp: Long = System.currentTimeMillis()
    private val CHANNEL_ID = "SafetyServiceChannel"

    override fun onCreate() {
        super.onCreate()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(R.string.app_active))
            .setContentText(getString(R.string.sec_rules))
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()

        startForeground(1, notification)
        startLocationUpdates()
        return START_STICKY
    }

    private fun startLocationUpdates() {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            60000 // intervalo de 1 minuto
        ).build()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val currentDeviceLocation = locationResult.lastLocation ?: return
                checkRulesAndAlert(currentDeviceLocation)
            }
        }

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                mainLooper
            )
        }
    }

    private fun checkRulesAndAlert(location: Location) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val db = FirebaseFirestore.getInstance()

        db.collection("users").document(userId).get().addOnSuccessListener { doc ->
            val userName = doc.getString("fullName") ?: doc.getString("name") ?: "Utilizador"
            //VERIFICAR CONSENTIMENTO
            val isMonitoringEnabled = doc.getBoolean("isMonitoringEnabled") ?: true
            if (!isMonitoringEnabled) {
                Log.d("SafetYSec", "Monitorização desativada pelo utilizador.")
                return@addOnSuccessListener
            }

            //VERIFICAR JANELA DE PRIVACIDADE
            val rules = doc.get("monitoringRules") as? Map<String, Any> ?: return@addOnSuccessListener
            val window = rules["authWindow"] as? Map<String, Any> ?: return@addOnSuccessListener
            if (!isWithinAuthWindow(window)) return@addOnSuccessListener

            val safeZone = rules["safeZone"] as? Map<String, Any>
            if (safeZone != null) {
                val center = safeZone["center"] as? Map<String, Any>
                val radius = (safeZone["radius"] as? Number)?.toDouble() ?: 500.0

                if (center != null) {
                    val centerLat = (center["latitude"] as? Number)?.toDouble() ?: 0.0
                    val centerLng = (center["longitude"] as? Number)?.toDouble() ?: 0.0

                    val centerLocation = Location("").apply {
                        latitude = centerLat
                        longitude = centerLng
                    }

                    // Cálculo da distância real entre o protegido e o centro
                    val distanceToCenter = location.distanceTo(centerLocation)

                    Log.d("SafetYSec", "Distância ao centro da zona segura: ${distanceToCenter}m (Raio: ${radius}m)")

                    if (distanceToCenter > radius) {
                        triggerAlert(
                            getString(R.string.alert_geofence_exit),
                            getString(R.string.alert_geofence_desc, distanceToCenter.toInt()),
                            location,
                            userName
                        )
                    }
                }
            }

            // Resto da lógica de Velocidade, Geofencing e Inatividade...
            val inactivityLimitMins = (rules["inactivityMins"] as? Number)?.toInt() ?: 30
            checkInactivity(location, inactivityLimitMins, userName)
        }
    }

    private fun checkInactivity(currentLocation: Location, limitMins: Int, userName: String) {
        //se for a primeira vez que obtemos a localização, apenas guardamos e saímos
        if (lastLocation == null) {
            lastLocation = currentLocation
            lastMovementTimestamp = System.currentTimeMillis()
            return
        }

        val distance = currentLocation.distanceTo(lastLocation!!)

        //Se o movimento for superior a 5 metros, consideramos que o utilizador está ATIVO
        if (distance > 5.0) {
            Log.d("SafetYSec", "Movimento detetado: ${distance.toInt()}m. Reset ao timer.")
            lastLocation = currentLocation
            lastMovementTimestamp = System.currentTimeMillis()
        } else {
            //Se estiver parado, calculamos há quanto tempo
            val timeElapsedMs = System.currentTimeMillis() - lastMovementTimestamp
            val minutesInactive = timeElapsedMs / 60000

            Log.d("SafetYSec", "Utilizador parado há $minutesInactive minutos.")

            if (minutesInactive >= limitMins) {
                //disparar o alerta com o nome real e localização
                triggerAlert(getString(R.string.alert_inactivity), getString(R.string.user_immobile_desc, minutesInactive.toInt()), currentLocation, userName)

                //reset
                lastMovementTimestamp = System.currentTimeMillis()
            }
        }
    }

    private fun isWithinAuthWindow(window: Map<String, Any>): Boolean {
        val days = window["days"] as? List<String> ?: emptyList()
        val start = window["start"] as? String ?: "00:00"
        val end = window["end"] as? String ?: "23:59"

        val calendar = Calendar.getInstance()
        val currentDay = SimpleDateFormat("EEEE").format(calendar.time).replaceFirstChar { it.uppercase() }
        val nowStr = SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar.time)

        return days.contains(currentDay) && nowStr >= start && nowStr <= end
    }

    private fun triggerAlert(type: String, message: String, location: Location, userName: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val alertId = UUID.randomUUID().toString()

        val alertData = hashMapOf(
            "id" to alertId,
            "protectedId" to userId,
            "protectedName" to userName,
            "type" to type,
            "location" to "${location.latitude}, ${location.longitude}",
            "timestamp" to System.currentTimeMillis(),
            "status" to "ACTIVE",
            "message" to message
        )

        FirebaseFirestore.getInstance().collection("alerts").document(alertId).set(alertData)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, "Safety Service", NotificationManager.IMPORTANCE_LOW)
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null
}