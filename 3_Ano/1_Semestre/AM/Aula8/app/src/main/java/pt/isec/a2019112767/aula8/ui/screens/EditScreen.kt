package pt.isec.a2019112767.aula8.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import pt.isec.a2019112767.aula8.ui.utils.FileUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreen(
    name: MutableState<String>,
    email: MutableState<String>,
    phone: MutableState<String>,
    birthday: DatePickerState,
    picture: MutableState<String?>,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val pickImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                picture.value = FileUtils.createFileFromUri(context,it)
            }
        }
    )

    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
    ) {
        OutlinedTextField(
            value = name.value,
            isError = name.value.isEmpty(),
            label = {
                Text("Name:")
            },
            onValueChange = { newText ->
                name.value = newText
            },
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = email.value,
            isError = email.value.isEmpty(),
            label = {
                Text("Email:")
            },
            onValueChange = { newText ->
                email.value = newText
            },
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = phone.value,
            isError = phone.value.isEmpty(),
            label = {
                Text("Phone:")
            },
            onValueChange = { newText ->
                phone.value = newText
            },
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        Text("Birthday:")
        DatePicker(
            state = birthday,
        )
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {pickImage.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            },
            modifier = Modifier.align(Alignment.CenterHorizontally),
        ) {
            Text("Select picture")
        }
        Spacer(Modifier.height(16.dp))
        picture.value?.let { path ->
            AsyncImage(
                model = path,
                contentDescription = "Contact's picture",
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}