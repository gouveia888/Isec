package pt.isec.a2019112767.aula8.model

import java.io.*
import java.text.Collator
import java.util.Date
import java.util.Locale

class ContactsList : Serializable {
    private val contacts = mutableListOf<Contact>()
    fun addContact(contact: Contact) {
        contacts.add(contact)
    }
    /*fun getContacts(): List<Contact> {
        return contacts.toList()
    }*/
    fun getContacts(): List<Contact> {
        val collator = Collator.getInstance(Locale.getDefault()).apply {
            strength = Collator.PRIMARY
        } //orddenaçao tendo em conta o idioma da localizaçao
        return contacts.sortedWith { contact1, contact2 ->
            collator.compare(contact1.name, contact2.name)
        }
    }

    fun save(destination: OutputStream) {
        try {
            ObjectOutputStream(destination).use { oos->
                oos.writeObject(this)
            }
        } catch (_: Exception) { }
    }
    companion object {
        fun load(source : InputStream) : ContactsList?  {
            try {
                ObjectInputStream(source).use { ois->
                    return ois.readObject() as ContactsList
                }
            } catch (_: Exception){ return null }
        }
    }

}