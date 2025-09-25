package pt.isec.a2019112767.Aula2

import android.app.Activity
import android.os.Bundle
import android.util.Log

class MyActivity : Activity() {

    val app : MyApp by lazy { application as MyApp } //by lazy - inicializaçao tardia - apenas quando necessaria

    private var _my_value = 0
    val my_value : Int
        get() = ++_my_value

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_activity)
        Log.i("AMovApp","onCreate: Atividade Criada!")
        Log.i("AMovApp", "contador : $my_value")
        Log.i("AMovApp", "contador da App : ${app.GlobalCounter}")
    }

    override fun onStart() {
        super.onStart()
        Log.i("AMovApp","onStart: Atividade Criada!")
        Log.i("AMovApp", "contador : $my_value")
    }

    override fun onRestart() {
        super.onRestart()
        Log.i("AMovApp","onRestart: Atividade Criada!")
        Log.i("AMovApp", "contador : $my_value")
    }

    override fun onResume() {
        super.onResume()
        Log.i("AMovApp","onResume: Atividade Criada!")
        Log.i("AMovApp", "contador : $my_value")
    }

    override fun onPause() {
        super.onPause()
        Log.i("AMovApp","onPause: Atividade Criada!")
        Log.i("AMovApp", "contador : $my_value")
    }

    override fun onStop() {
        super.onStop()
        Log.i("AMovApp","onStop: Atividade Criada!")
        Log.i("AMovApp", "contador : $my_value")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("AMovApp","onDestroy: Atividade Criada!")
        Log.i("AMovApp", "contador : $my_value")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.i("AMovApp","onSaveInstanceState: Atividade Criada!")
        Log.i("AMovApp", "contador : $my_value")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.i("AMovApp","onRestoreInstanceState: Atividade Criada!")
        Log.i("AMovApp", "contador : $my_value")
        }
}