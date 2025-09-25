package pt.isec.a2019112767.Aula2

import android.app.Application
import android.util.Log

class MyApp : Application() {

    private var _my_value = 0
    val GlobalCounter : Int
        get() = ++_my_value

    override fun onCreate() {
        super.onCreate()
        Log.i("AMovApp","onCreate: Aplicação Criada!")
        Log.i("AMovApp", "contador $GlobalCounter")
    }
}