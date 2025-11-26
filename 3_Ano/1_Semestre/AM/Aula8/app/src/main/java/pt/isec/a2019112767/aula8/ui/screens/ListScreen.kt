package pt.isec.a2019112767.aula8.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import pt.isec.a2019112767.aula8.R
import pt.isec.a2019112767.aula8.model.Contact
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ListScreen(
    contacts : List<Contact>,
    showExpanded : Boolean = false,
    onSelectContact : (Contact) -> Unit = {},
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
    ) {
        items(
            contacts,
            key = { contact -> contact.hashCode() }
        ) { contact ->
            ContactCard(
                contact = contact,
                showExpanded = showExpanded,
                onSelectContact = onSelectContact,
            )
        }
        item {
            val positions = arrayOf(
                LatLng(40.1925, -8.4128), LatLng(39.1925, -8.4128),
                LatLng(41.1925, -8.4128), LatLng(40.1925, -7.4128),
                LatLng(40.1925, -9.4128),
            )
            val boundsBuilder = LatLngBounds.Builder()
            positions.forEach { boundsBuilder.include(it) }
            val bounds = boundsBuilder.build()
            val cameraPositionState = rememberCameraPositionState {
                CameraPosition.fromLatLngZoom(positions[0], 13f)
            }
            LaunchedEffect(bounds) {
                cameraPositionState.animate(
                    update = CameraUpdateFactory.newLatLngBounds(bounds,64),
                    durationMs = 5000
                )
            }
            GoogleMap(
                modifier = Modifier.fillMaxSize().height(400.dp),
                cameraPositionState = cameraPositionState
            ) {

                positions.forEach { latLng ->
                    Marker(state = MarkerState(position = latLng))
                }
            }
        }
    }
}

@Composable
fun ContactCard(
    contact: Contact,
    showExpanded: Boolean,
    onSelectContact: (Contact) -> Unit,
    modifier: Modifier = Modifier
){
    val formatter = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
    Card(
        modifier = modifier.fillMaxWidth().padding(8.dp), elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(146, 120, 192)),
        onClick = { onSelectContact(contact) }
    ) {

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = contact.picture?: R.drawable.avatar_icon,
                    contentScale = ContentScale.Crop,
                    contentDescription = "Contact image",
                    modifier = Modifier
                        .fillMaxWidth(0.2f)
                        .aspectRatio(1f)
                        .clip(CircleShape)
                        //.align(Alignment.CenterHorizontally)
                )
                Spacer(Modifier.width(10.dp))
                Text(text = contact.name, fontSize = 20.sp)
            }

            if (showExpanded) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = contact.phone, fontSize = 14.sp)
                    Text(text = contact.email, fontSize = 14.sp)
                }
                contact.birthday?.let { birthday ->
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = formatter.format(birthday),
                        fontSize = 14.sp
                    )
                } ?: Text(modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "----.--.--",
                    fontSize = 14.sp)
            }
        }
    }
}

