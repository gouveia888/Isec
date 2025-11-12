package pt.isec.a2019112767.aula8.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import pt.isec.a2019112767.aula8.model.Contact
import pt.isec.a2019112767.aula8.R
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ShowScreen(
    contact: Contact,
    modifier: Modifier = Modifier
) {
    if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE)
        ShowScreenLandscape(contact,modifier)
    else
        ShowScreenPortrait(contact,modifier)
}

@Composable
private fun ShowScreenPortrait(
    contact: Contact,
    modifier: Modifier = Modifier
) {
    val dateFormatter = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // https://static-00.iconduck.com/assets.00/avatar-icon-512x512-gu21ei4u.png
        // https://developer.android.com/static/images/home/android-15.svg
        AsyncImage(
            model = contact.picture?: R.drawable.avatar_icon,
            contentScale = ContentScale.Crop,
            contentDescription = "Contact image",
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .aspectRatio(1f)
                .clip(CircleShape)
                .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text("Name:", fontStyle = FontStyle.Italic, fontSize = 16.sp)
        Text(contact.name, fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 16.dp),)

        Spacer(modifier = Modifier.height(16.dp))

        Text("Email:", fontStyle = FontStyle.Italic, fontSize = 16.sp)
        Text(contact.email, fontSize = 16.sp, fontWeight = FontWeight.Medium, modifier = Modifier.padding(start = 16.dp),)
        Spacer(modifier = Modifier.height(16.dp))

        Text("Phone:", fontStyle = FontStyle.Italic, fontSize = 16.sp)
        Text(contact.phone, fontSize = 16.sp, fontWeight = FontWeight.Medium, modifier = Modifier.padding(start = 16.dp),)
        Spacer(modifier = Modifier.height(16.dp))

        Text("Birthday:", fontStyle = FontStyle.Italic, fontSize = 16.sp)
        contact.birthday?.let { birthday ->
            Text(
                text = dateFormatter.format(birthday),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(start = 16.dp),
            )
        } ?: Text(
            text = "----.--.--",
            fontSize = 16.sp,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Composable
private fun ShowScreenLandscape(
    contact: Contact,
    modifier: Modifier = Modifier
) {
    val dateFormatter = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
    Row (
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // https://static-00.iconduck.com/assets.00/avatar-icon-512x512-gu21ei4u.png
        // https://developer.android.com/static/images/home/android-15.svg
        AsyncImage(
            model = contact.picture ?: R.drawable.avatar_icon,
            contentScale = ContentScale.Crop,
            contentDescription = "Contact image",
            modifier = Modifier
                .fillMaxHeight(0.5f)
                .aspectRatio(1f)
                .clip(CircleShape)
                .align(Alignment.CenterVertically)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp)
        ) {
            Text("Name:", fontStyle = FontStyle.Italic, fontSize = 16.sp)
            Text(
                contact.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(start = 16.dp),
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text("Email:", fontStyle = FontStyle.Italic, fontSize = 16.sp)
            Text(
                contact.email,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(start = 16.dp),
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text("Phone:", fontStyle = FontStyle.Italic, fontSize = 16.sp)
            Text(
                contact.phone,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(start = 16.dp),
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text("Birthday:", fontStyle = FontStyle.Italic, fontSize = 16.sp)
            contact.birthday?.let { birthday ->
                Text(
                    text = dateFormatter.format(birthday),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(start = 16.dp),
                )
            } ?: Text(
                text = "----.--.--",
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}