package pt.isec.a2019112767.aula8

import android.app.Application
import com.google.android.gms.location.LocationServices
import pt.isec.a2019112767.aula8.model.ContactsList
import pt.isec.a2019112767.aula8.ui.utils.location.FusedLocationHandler
import pt.isec.a2019112767.aula8.ui.utils.location.LocationHandler
import kotlin.getValue

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
    /*
    val locationHandler : LocationHandler by lazy {
    val locationManager = getSystemService(LOCATION_SERVICE)
    as LocationManager
    LocationManagerHandler(locationManager)
    }
    */

    val locationHandler : LocationHandler by lazy {
        val locationProvider = LocationServices
            .getFusedLocationProviderClient(this)
        FusedLocationHandler(locationProvider)
    }


    fun saveData() {
        try {
            contactsList.save(openFileOutput(DATAFILE, MODE_PRIVATE))
        } catch (_: Exception) {}
    }
    override fun onCreate() {
        super.onCreate()
    }

}