package pt.isec.safetysec.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import com.google.maps.android.compose.*
import pt.isec.safetysec.R


@Composable
fun GeofencingScreen(navController: NavController, protectedId: String) {
    val db = FirebaseFirestore.getInstance()
    val context = LocalContext.current

    var protectedLocation by remember { mutableStateOf(LatLng(39.5, -8.5)) }
    var safeZoneCenter by remember { mutableStateOf(LatLng(39.5, -8.5)) }
    var radius by remember { mutableFloatStateOf(500f) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(protectedLocation, 12f)
    }

    // carregar dados atuais do Protegido
    LaunchedEffect(protectedId) {
        if (protectedId.isNotEmpty()) {
            db.collection("users").document(protectedId).get().addOnSuccessListener { snapshot ->
                val lat = snapshot.getDouble("latitude") ?: 39.5
                val lng = snapshot.getDouble("longitude") ?: -8.5
                protectedLocation = LatLng(lat, lng)

                val sz = snapshot.get("safeZone") as? Map<String, Any>
                if (sz != null) {
                    safeZoneCenter = LatLng(sz["centerLat"] as Double, sz["centerLng"] as Double)
                    radius = (sz["radius"] as? Number)?.toFloat() ?: 500f
                } else {
                    // se nao houver zona e posição atual
                    safeZoneCenter = protectedLocation
                }

                cameraPositionState.position = CameraPosition.fromLatLngZoom(safeZoneCenter, 15f)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapClick = { clickedLatLng ->
                safeZoneCenter = clickedLatLng
            }
        ) {
            Marker(
                state = MarkerState(position = protectedLocation),
                title = stringResource(id = R.string.protected_location)
            )

            Circle(
                center = safeZoneCenter,
                radius = radius.toDouble(),
                fillColor = Color.Blue.copy(alpha = 0.1f),
                strokeColor = Color.Blue,
                strokeWidth = 3f
            )
        }

        Card(
            modifier = Modifier
                .align(androidx.compose.ui.Alignment.BottomCenter)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(stringResource(id = R.string.setup_safe_zone), style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))

                Slider(
                    value = radius,
                    onValueChange = { radius = it },
                    valueRange = 100f..3000f,
                    steps = 10
                )
                Text(text = stringResource(id = R.string.radius_label, radius.toInt()))

                Button(
                    onClick = {
                        val szData = hashMapOf(
                            "centerLat" to safeZoneCenter.latitude,
                            "centerLng" to safeZoneCenter.longitude,
                            "radius" to radius
                        )

                        // Atualiza utilizador PROTEGIDO
                        db.collection("users").document(protectedId)
                            .update("safeZone", szData)
                            .addOnSuccessListener {
                                Toast.makeText(context, "Zona Guardada com Sucesso!", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(context, "Erro: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                    },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                ) {
                    Text(text = stringResource(id = R.string.save_changes))
                }
            }
        }
    }
}