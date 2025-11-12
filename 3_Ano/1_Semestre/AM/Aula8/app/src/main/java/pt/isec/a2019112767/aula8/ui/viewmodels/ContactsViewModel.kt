package pt.isec.a2019112767.aula8.ui.viewmodels

import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pt.isec.a2019112767.aula8.model.Contact
import pt.isec.a2019112767.aula8.model.ContactsList
import java.util.Date
import java.util.Locale


class ContactsViewModelFactory(
    val contactsList : ContactsList
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        return ContactsViewModel(contactsList) as T
    }
}
@OptIn(ExperimentalMaterial3Api::class)
class ContactsViewModel(
    val contactsList : ContactsList,
    var currentContact : Contact? = null,

) : ViewModel() {

    var name = mutableStateOf("")
    var email = mutableStateOf("")
    var phone = mutableStateOf("")
    val birthdayDPState = DatePickerState(
        Locale.getDefault(),
        Date().time
    )
    val picture = mutableStateOf<String?>("")

    fun createContact() {
        currentContact = null
        name.value = ""
        phone.value = ""
        email.value = ""
        birthdayDPState.selectedDateMillis = 0
        picture.value = ""
    }

    fun selectContact(contact : Contact) {
        currentContact = contact
        name.value = contact.name
        phone.value = contact.phone
        email.value = contact.email
        birthdayDPState.selectedDateMillis = contact.birthday?.time ?: 0
        picture.value = contact.picture!! //!!obriga a nao ser nulo
    }

    fun saveContact() : Boolean {
        if (name.value.isEmpty() || email.value.isEmpty() || phone.value.isEmpty()) {
            return false
        }
        currentContact?.let { contact ->
            contact.name = name.value
            contact.phone = phone.value
            contact.email = email.value
            contact.birthday = birthdayDPState.selectedDateMillis?.let { Date(it) }
            contact.picture = picture.value
        } ?: contactsList.addContact(
            Contact(
                name.value,
                phone.value,
                email.value,
                birthdayDPState.selectedDateMillis?.let { Date(it) },
                picture.value,
            )
        )
        return true
    }
}
