package pt.isec.safetysec.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.camera.video.FileOutputOptions
import androidx.camera.video.Recording
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.video.AudioConfig
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import pt.isec.safetysec.services.*
import java.io.File
import java.util.UUID
import pt.isec.safetysec.R
@Composable
fun ProtectedDashboardScreen(navController: NavController) {
    //estados interface
    var otpCode by remember { mutableStateOf("------") }
    var showAlertDialog by remember { mutableStateOf(false) }
    var timeLeft by remember { mutableIntStateOf(10) }
    var isMonitoringEnabled by remember { mutableStateOf(true) }
    var monitorDataList by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var protectedName by remember { mutableStateOf("Carregando...") }

    // MFA
    var inputSecurityCode by remember { mutableStateOf("") }
    var correctSecurityCode by remember { mutableStateOf("") }

    val context = LocalContext.current
    val userId = FirebaseService.getCurrentUserId() ?: ""
    val db = FirebaseFirestore.getInstance()
    val locationService = remember { LocationService(context) }
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    val mainExecutor = remember { ContextCompat.getMainExecutor(context) }

    //camera e gravacao
    val cameraController = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(LifecycleCameraController.VIDEO_CAPTURE)
        }
    }
    var activeRecording by remember { mutableStateOf<Recording?>(null) }
    var isRecording by remember { mutableStateOf(false) }
    var pendingAlertType by remember { mutableStateOf("PÂNICO") }

    //gravaçao
    fun startPanicRecording(alertId: String) {
        val hasAudio = ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
        if (!hasAudio) return

        isRecording = true
        val outputFilePath = FirebaseService.getTempFilename(context)
        val outputOptions = FileOutputOptions.Builder(File(outputFilePath)).build()

        try {
            activeRecording = cameraController.startRecording(
                outputOptions,
                AudioConfig.create(true),
                mainExecutor
            ) { event ->
                if (event is VideoRecordEvent.Finalize) {
                    isRecording = false
                    if (!event.hasError()) {
                        FirebaseService.uploadVideoOnly(alertId, outputFilePath) {
                            Toast.makeText(context, "Evidência de vídeo enviada!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            Handler(Looper.getMainLooper()).postDelayed({ activeRecording?.stop() }, 5000)
        } catch (e: Exception) { isRecording = false }
    }

    //sensores
    val fallDetector = remember {
        FallDetectionService(context) { type ->
            if (!showAlertDialog) {
                pendingAlertType = type
                showAlertDialog = true
                timeLeft = 10
            }
        }
    }

    //ciclo vida
    LaunchedEffect(Unit) { cameraController.bindToLifecycle(lifecycleOwner) }
    DisposableEffect(Unit) {
        fallDetector.start()
        onDispose { fallDetector.stop() }
    }

    // --- Listener de Dados (Consentimento, Nome, PIN e Monitores) ---
    LaunchedEffect(userId) {
        if (userId.isNotEmpty()) {
            db.collection("users").document(userId).addSnapshotListener { snapshot, _ ->
                if (snapshot != null && snapshot.exists()) {
                    protectedName = snapshot.getString("name") ?: "Utilizador"
                    isMonitoringEnabled = snapshot.getBoolean("isMonitoringEnabled") ?: true
                    correctSecurityCode = snapshot.getString("securityCode") ?: "1234"

                    val monitorIds = snapshot.get("monitors") as? List<String> ?: emptyList()
                    if (monitorIds.isNotEmpty()) {
                        db.collection("users").whereIn("uid", monitorIds).get()
                            .addOnSuccessListener { query ->
                                monitorDataList = query.documents.mapNotNull { it.data }
                            }
                    } else { monitorDataList = emptyList() }
                }
            }
        }
    }

    //logica de alerta
    LaunchedEffect(showAlertDialog) {

        if (showAlertDialog) {
            while (timeLeft > 0) {
                delay(1000L)
                timeLeft--
            }

            if (showAlertDialog && isMonitoringEnabled) {
                db.collection("users").document(userId).get().addOnSuccessListener { snapshot ->
                    val rules = snapshot.get("monitoringRules") as? Map<String, Any>

                    if (isWithinPrivacyWindow(rules)) {
                        val alertId = UUID.randomUUID().toString()
                        locationService.getCurrentLocation { coords ->
                            val initialAlert = hashMapOf(
                                "id" to alertId,
                                "protectedId" to userId,
                                "protectedName" to protectedName,
                                "type" to pendingAlertType,
                                "location" to coords,
                                "timestamp" to System.currentTimeMillis(),
                                "status" to "ACTIVE"
                            )
                            db.collection("alerts").document(alertId).set(initialAlert)
                                .addOnSuccessListener { startPanicRecording(alertId) }
                        }
                    } else {
                        Toast.makeText(context, "Fora do horário de monitorização.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            showAlertDialog = false
            timeLeft = 10
        }
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    if (!isRecording && !showAlertDialog) {
                        pendingAlertType = "PÂNICO"
                        showAlertDialog = true
                        timeLeft = 10
                    }
                },
                containerColor = if (isRecording) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.error,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Warning, contentDescription = null)
                Text(if (isRecording) stringResource(id=R.string.status_recording) else stringResource(id=R.string.panic_button))
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp).verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(id = R.string.hello_user, protectedName),
                    style = MaterialTheme.typography.headlineSmall
                )
                IconButton(onClick = { navController.navigate("profile") }) {
                    Icon(Icons.Default.Settings, contentDescription = "Perfil")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            //consentimento rules
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(
                containerColor = if (isMonitoringEnabled) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
            )) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(stringResource(id=R.string.consent_title), fontWeight = FontWeight.Bold)
                        Text(if (isMonitoringEnabled) stringResource(id=R.string.monitoring_active) else stringResource(id=R.string.monitoring_paused), style = MaterialTheme.typography.bodySmall)
                    }
                    Switch(checked = isMonitoringEnabled, onCheckedChange = {
                        db.collection("users").document(userId).update("isMonitoringEnabled", it)
                    })
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            //OTP
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(stringResource(id=R.string.association_code), style = MaterialTheme.typography.titleSmall)
                    Text(text = otpCode, style = MaterialTheme.typography.displayMedium, color = MaterialTheme.colorScheme.primary)
                    Button(onClick = {
                        val newCode = (100000..999999).random().toString()
                        otpCode = newCode
                        FirebaseService.updateOTPCode(userId, newCode)
                    }) { Text(stringResource(id=R.string.generate_new_code)) }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Lista de Monitores
            Text(stringResource(id=R.string.my_monitors), style = MaterialTheme.typography.titleMedium)
            if (monitorDataList.isEmpty()) {
                Text(stringResource(id=R.string.no_monitors_found), color = Color.Gray)
            } else {
                monitorDataList.forEach { monitor ->
                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Person, contentDescription = null)
                            Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
                                Text(text = monitor["name"].toString(), fontWeight = FontWeight.Bold)
                                Text(text = monitor["email"].toString(), style = MaterialTheme.typography.bodySmall)
                            }
                            IconButton(onClick = { FirebaseService.removeMonitor(userId, monitor["uid"].toString()) { } }) {
                                Icon(Icons.Default.Delete, contentDescription = "Remover", tint = Color.Red)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text(stringResource(id=R.string.config_title), style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            // Botão para Configurar Regras (Horário, Velocidade, etc.)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                DashboardActionCard(
                    title = stringResource(id=R.string.monitoring_rules),
                    icon = Icons.AutoMirrored.Filled.List,
                    modifier = Modifier.weight(1f)
                ) {
                    navController.navigate("rules_config")
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        }

        //CANCELAMENTO POR PIN (MFA)
        if (showAlertDialog) {
            AlertDialog(
                onDismissRequest = { },
                title = { Text(stringResource(id=R.string.alert_in_progress), color = Color.Red, fontWeight = FontWeight.Bold) },
                text = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(stringResource(id=R.string.alert_will_send_in), textAlign = TextAlign.Center)
                        Text("$timeLeft", style = MaterialTheme.typography.displayLarge, color = Color.Red)
                        Text(stringResource(id=R.string.seconds_label), textAlign = TextAlign.Center)

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = inputSecurityCode,
                            onValueChange = { if (it.length <= 4) inputSecurityCode = it },
                            label = { Text(stringResource(id=R.string.security_pin)) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                        onClick = {
                            if (inputSecurityCode == correctSecurityCode) {
                                showAlertDialog = false
                                inputSecurityCode = ""
                                Toast.makeText(context, "Alerta Cancelado com Sucesso", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "PIN Incorreto!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    ) { Text(stringResource(id=R.string.stop_alert_btn)) }
                }
            )
        }
    }
}

@SuppressLint("DefaultLocale")
private fun isWithinPrivacyWindow(rules: Map<String, Any>?): Boolean {
    if (rules == null) return true
    val calendar = java.util.Calendar.getInstance()
    val currentDay = calendar.get(java.util.Calendar.DAY_OF_WEEK).toString()
    val currentTime = String.format("%02d:%02d",
        calendar.get(java.util.Calendar.HOUR_OF_DAY),
        calendar.get(java.util.Calendar.MINUTE)
    )

    val activeDays = rules["activeDays"] as? List<String> ?: emptyList()
    val startTime = rules["startTime"] as? String ?: "00:00"
    val endTime = rules["endTime"] as? String ?: "23:59"

    if (activeDays.isNotEmpty() && !activeDays.contains(currentDay)) return false
    return currentTime in startTime..endTime
}

@Composable
fun DashboardActionCard(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = modifier.height(100.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = title, style = MaterialTheme.typography.labelLarge, textAlign = TextAlign.Center)
        }
    }
}