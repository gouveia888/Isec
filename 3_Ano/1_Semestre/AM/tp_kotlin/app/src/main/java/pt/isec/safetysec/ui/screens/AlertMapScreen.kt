import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.firebase.firestore.FirebaseFirestore
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.android.gms.maps.model.LatLng
import pt.isec.safetysec.R

@Composable
fun AlertMapScreen(protectedId: String, alertLat: Double, alertLng: Double) {
    var safeZone by remember { mutableStateOf<Map<String, Any>?>(null) }
    val alertPos = LatLng(alertLat, alertLng)

    // procura a SafeZone do protegido no Firestore
    LaunchedEffect(protectedId) {
        FirebaseFirestore.getInstance().collection("users")
            .document(protectedId).get()
            .addOnSuccessListener { doc ->
                safeZone = doc.get("safeZone") as? Map<String, Any>
            }
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(alertPos, 15f)
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        Marker(
            state = MarkerState(position = alertPos),
            title = stringResource(id = R.string.alert_location_title),
            snippet = stringResource(id = R.string.protected_is_here)
        )

        safeZone?.let { zone ->
            val center = LatLng(zone["centerLat"] as Double, zone["centerLng"] as Double)
            val radius = (zone["radius"] as Number).toDouble()

            Circle(
                center = center,
                radius = radius,
                fillColor = Color(0x2200FF00),
                strokeColor = Color.Green,
                strokeWidth = 2f
            )

            //zona segura
            Marker(
                state = MarkerState(position = center),
                title = stringResource(id = R.string.safe_zone_center),
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
            )
        }
    }
}