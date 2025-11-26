package pt.isec.a2019112767.aula8.ui.utils.location

import android.location.Location

interface LocationHandler {
    var locationEnabled : Boolean
    var onLocation : ((Location) -> Unit)?
    fun startLocationUpdates()
    fun stopLocationUpdates()
}

