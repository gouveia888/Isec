package pt.isec.a2019112767.aula8

import android.app.Application
import pt.isec.a2019112767.aula8.model.ContactsList

//Regisstar a app no manifest
class ContactsApp : Application() {
    companion object {
        private const val DATAFILE = "contacts.bin"
    }
    private val _contactsList by lazy {
        try {
            openFileInput(DATAFILE)?.let { srcFile-> ContactsList.Companion.load(srcFile) }
        } catch (_: Exception) {
            null
        } ?: ContactsList()
    }
    val contactsList get() = _contactsList

    fun saveData() {
        try {
            contactsList.save(openFileOutput(DATAFILE, MODE_PRIVATE))
        } catch (_: Exception) {}
    }
    override fun onCreate() {
        super.onCreate()
    }

}