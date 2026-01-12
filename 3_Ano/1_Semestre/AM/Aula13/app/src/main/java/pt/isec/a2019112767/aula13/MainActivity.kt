package pt.isec.a2019112767.aula13

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import pt.isec.a2019112767.aula13.ui.theme.Aula13Theme

class MainActivity : ComponentActivity() {

    private lateinit var auth : FirebaseAuth

    val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        auth = Firebase.auth
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Aula13Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    viewModel = viewModel
                    onSuccess = {
                    },
                    modifier = Modifier
                }
            }
        }
    }

    override fun onStart () {
        super.onStart ()
        //showUser(auth.currentUser)
        //createUserWithEmail("teste@gmail.com", "Teste_123")
        //signInWithEmail("teste@gmail.com", "Teste_123")
    }

    fun showUser(user: FirebaseUser?) {
        val str = when (user) {
            null -> "Not authenticated user "
            else -> " User : ${user.email}"
        }
        //binding.tvStatus.text = str
        Log.i(TAG, str)
    }

    override fun onStop() {
        super.onStop()
        //signOut()
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Aula13Theme {
        Greeting("Android")
    }
}
