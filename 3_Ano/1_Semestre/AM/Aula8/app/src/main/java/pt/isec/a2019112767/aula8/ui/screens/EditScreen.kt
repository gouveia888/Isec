package pt.isec.a2019112767.aula8.ui.screens

import android.R
import android.app.Activity
import android.net.Uri
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.content.FileProvider
import coil3.compose.AsyncImage
import pt.isec.a2019112767.aula8.ui.utils.FileUtils
import java.io.File

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
    //corresponde ao GetContent do pdf
    val pickImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                picture.value = FileUtils.createFileFromUri(context,it)
            }
        }
    )

    val imagePath = FileUtils.getTempFilename(context)

    val takePicture = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { activityResult->
        if (activityResult.resultCode == Activity.RESULT_OK) {
            picture.value = FileUtils.copyFile(context,imagePath)
        }
    }

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
        Row(){
            Button(
                onClick = {pickImage.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                },
                //modifier = Modifier.align(Alignment.CenterHorizontally),
            ) {
                Text("Select picture")
            }

            Button(
                onClick = {
                    //val fileUri = Uri.fromFile( File(imagePath))
                    val fileUri = FileProvider.getUriForFile(
                        context,
                        "package pt.isec.a2019112767.aula8.fileprovider",
                        File(imagePath)
                    )
                    //Permissoes
                    if (checkSelfPermission(context,android.Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(
                            context as Activity,
                            arrayOf(android.Manifest.permission.CAMERA),
                             1
                        )
                    }

                    if(checkSelfPermission(context,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
                        var result = takePicture.launch(
                            Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                                putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
                            }
                        )
                        Log.i("EditScreen", "result: $result")
                    }else{
                        takePicture.launch(
                            Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                                putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
                            })
                    }
                },
                //modifier = Modifier.align(Alignment.CenterHorizontally),
            ) {
                Text("Take picture")
            }
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

