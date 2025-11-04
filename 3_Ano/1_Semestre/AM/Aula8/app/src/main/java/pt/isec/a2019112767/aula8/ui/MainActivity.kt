package pt.isec.a2019112767.aula8.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
                MainScreen(viewModel) //MainScreen já é um scaffold
            }
        }
    }
    override fun onPause() {
        super.onPause()
        app.saveData()
    }
}