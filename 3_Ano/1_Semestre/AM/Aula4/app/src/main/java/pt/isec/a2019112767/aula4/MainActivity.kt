package pt.isec.a2019112767.aula4

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModel


class MainActivity : AppCompatActivity() {
    val viewModel : MyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        //CustomView
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //ViewModels
        //super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.i("Test", "onCreate: ${viewModel.value}")

        //MutavelLiveData
        viewModel.state.observe(this) {  Log.i("Test", it)  }
    }
    //CustomView
    override fun onResume() {
        super.onResume()
        Log.i("Test", "onResume: ${viewModel.value}")
        //MutavelLiveData
        viewModel.changeState("onResume${this.hashCode()}")
    }
    //CustomView
    override fun onPause() {
        super.onPause()
        Log.i("Test", "onPause: ${viewModel.value}")
        //MutavelLiveData
        viewModel.changeState( "onPause${this.hashCode()}")
    }
    override fun onStop() {
        super.onStop()
        //MutavelLiveData
        viewModel.changeState( "onStop${this.hashCode()}")
        //viewModel.state.removeObservers(this)
    }

}