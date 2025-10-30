package pt.isec.a2019112767.aula8.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pt.isec.a2019112767.aula8.model.Contact
import pt.isec.a2019112767.aula8.model.ContactsList
import java.util.Date

class ContactsViewModelFactory(
    val contactsList : ContactsList
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        return ContactsViewModel(contactsList) as T
    }
}
class ContactsViewModel(
    val contactsList : ContactsList

) : ViewModel() {

    var currentContact : Contact? = null
    var tempName by mutableStateOf("")
    var tempEmail by mutableStateOf("")
    var tempPhone by mutableStateOf("")

    fun selectContact(contact : Contact?) {
        currentContact = contact
    }

    fun addContact(
        name: String,
        email: String,
        phone: String,
        birthday: Date?
    ) {
        val newContact = Contact(name, email, phone, birthday)

        contactsList.addContact(newContact)
    }

    fun prepareForEdit(contact: Contact?) {
        if (contact != null) {
            tempName = contact.name
            tempEmail = contact.email
            tempPhone = contact.phone
            // ...
        } else { // Novo Contacto
            tempName = ""
            tempEmail = ""
            tempPhone = ""
        }
    }

    fun saveEditedContact(birthday: Date?) {
        // Lógica para salvar:
        if (currentContact == null) {
            // NOVO CONTACTO
            addContact(tempName, tempEmail, tempPhone, birthday)
        } else {
            // EDIÇÃO DO CONTACTO ATUAL
            currentContact!!.name = tempName
            currentContact!!.email = tempEmail
            currentContact!!.phone = tempPhone
            // ...
        }
    }
}
