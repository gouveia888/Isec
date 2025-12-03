package pt.isec.a2019112767.aula13.ui.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch
import pt.isec.a2019112767.aula13.ui.utils.FAuthUtil

data class User(val name: String, val email: String, val picture: String?)


class FireBaseViewModel : ViewModel(){
    private val _user = mutableStateOf(FAuthUtil.currentUser?.toUser())
    val user : MutableState<User?>
        get() = _user

    private val _error = mutableStateOf<String?>(null)
    val error : MutableState<String?>
        get() = _error

    fun createUserWithEmail(email: String, password: String) {
        if (email.isBlank() || password.isBlank())
            return

        viewModelScope.launch {
            FAuthUtil.createUserWithEmail(email, password) { exception ->
                if (exception == null)
                    _user.value = FAuthUtil.currentUser?.toUser()
                _error.value = exception?.message
            }
        }
    }

    fun signInWithEmail(email: String, password: String){
        if (email.isBlank() || password.isBlank())
            return

        viewModelScope.launch {
            FAuthUtil.signInWithEmail(email, password) { exception ->
                if(exception == null)
                    _user.value = FAuthUtil.currentUser?.toUser()
                _error.value = exception?.message
        }
    }
}

    fun singOut(){
        FAuthUtil.signOut()
        _user.value = null
        _error.value = null
    }
}

private fun FirebaseUser.toUser() : User?   {
    val displayName = this.displayName?:""
    val strEmail = this.email?:""
    val picture = this.photoUrl ?.toString()
    return User(displayName, strEmail, picture)
}
