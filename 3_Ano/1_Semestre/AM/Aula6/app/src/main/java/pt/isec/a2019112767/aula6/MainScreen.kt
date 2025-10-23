package pt.isec.a2019112767.aula6

import android.R.attr.text
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import pt.isec.a2019112767.aula6.Drawing.DrawingViewModel

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MainScreen(viewModel: DrawingViewModel) {
    var currentColor by remember { mutableStateOf(Color.Black) }
    val linesFlow by viewModel.linesFlow.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("PaintAMov", color = currentColor) },
                colors = TopAppBarDefaults.topAppBarColors(
                    //titleContentColor = Color(0,0,128),
                    containerColor = Color(255, 255, 224)
                ),
                actions = {
                    Button(
                        colors = ButtonDefaults.buttonColors(Color.Red),
                        modifier = Modifier.width(40.dp).aspectRatio(1f).padding(4.dp),
                        onClick = { currentColor = Color.Red }
                    ){}
                    Button(
                        colors = ButtonDefaults.buttonColors(Color.Green),
                        modifier = Modifier.width(40.dp).aspectRatio(1f).padding(4.dp),
                        onClick = { currentColor = Color.Green }
                    ){}
                    Button(
                        colors = ButtonDefaults.buttonColors(Color.Blue),
                        modifier = Modifier.width(40.dp).aspectRatio(1f).padding(4.dp),
                        onClick = { currentColor = Color.Blue }
                    ){}
                    Button(
                        colors = ButtonDefaults.buttonColors(Color.Black),
                        modifier = Modifier.width(40.dp).aspectRatio(1f).padding(4.dp),
                        onClick = { currentColor = Color.Black }
                    ){}
                    Button(
                        onClick = { viewModel.clear() },
                        colors = ButtonDefaults.buttonColors(Color.Blue)
                    ){
                        Text(
                            text="Clear"

                        )
                    }
                }
            )
        }
    )
    { innerPadding ->
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        viewModel.addLine(
                            change.position - dragAmount,
                            change.position,
                            currentColor
                        )
                    }
                }
        ) {
            linesFlow.forEach { line ->
                drawLine(
                    start = line.begin,
                    end = line.end,
                    color = line.color,
                    strokeWidth = 10f
                )
            }
        }
    }
}