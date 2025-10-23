package pt.isec.a2019112767.aula6

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import pt.isec.a2019112767.aula6.ui.theme.Aula6Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Aula6Theme {
                /*Scaffold(modifier = Modifier.fillMaxSize()) {
                    innerPadding ->
                    Greeting(
                        name = "Amov",
                        modifier = Modifier.padding(innerPadding)
                    )*/
                    MainScreen(viewModel())
                }
            }
        }
    }
/*}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {

    var counter by remember { mutableStateOf(0) }
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        for(i in 1 .. 5){
            Text(
                text = "$counter Hello $name!",
                fontSize = 20.sp,
            )
        }
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .background(Color.Yellow)
                .fillMaxWidth()
                .padding(8.dp)
                .background(Color(32,192,160))
                .padding(8.dp)
        ){
            Text(
                text="AAA"
            )
            Text(
                text="BBB"
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                counter++
                Log.i("AAA", "Counter: $counter")
            }

        ) {
            Text(
                text="Press me"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Aula6Theme {
        Greeting("Android")
    }
}
 */