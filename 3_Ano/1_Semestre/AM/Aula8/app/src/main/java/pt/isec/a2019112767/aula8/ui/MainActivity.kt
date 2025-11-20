package pt.isec.a2019112767.aula8.ui

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import pt.isec.a2019112767.aula8.ContactsApp
import pt.isec.a2019112767.aula8.ui.screens.MainScreen
import pt.isec.a2019112767.aula8.ui.theme.Aula8Theme
import pt.isec.a2019112767.aula8.ui.viewmodels.ContactsViewModel
import pt.isec.a2019112767.aula8.ui.viewmodels.ContactsViewModelFactory

class MainActivity : ComponentActivity() {
    private val app by lazy { application as ContactsApp }
    private val viewModel : ContactsViewModel by viewModels { ContactsViewModelFactory(app.contactsList) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Aula8Theme {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (checkSelfPermission(android.Manifest.permission.READ_MEDIA_IMAGES) !=
                        PackageManager.PERMISSION_GRANTED) {
                        askSinglePermission.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
                    }
                } else if (
                    checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED) {
                    askMultiplePermission.launch(
                        arrayOf( android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE )
                    )
                }


                MainScreen(viewModel) //MainScreen já é um scaffold
            }
        }
    }
    override fun onPause() {
        super.onPause()
        app.saveData()
    }

    private val askSinglePermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> /* TODO */}
    private val askMultiplePermission = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { map -> /* TODO */}
}