package pt.isec.a2019112767.aula4

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class MyViewModel : ViewModel() {
    //MyViewModel
    private var _value = 0
    val value : Int
        get() = _value++

    //MutavelLiveData
    private val _state : MutableLiveData<String> by lazy { MutableLiveData<String>("none") } //lazy so vai ser instanciada quando for necessaria
    val state : LiveData<String>
    get() = _state

    fun changeState(newState: String) { Log.i("Test", "changeState: $newState$_value ${this.hashCode()}")
        _state.setValue("$newState$value")
    }
}