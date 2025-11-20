package pt.isec.a2019112767.aula8.model


import java.io.Serializable
import java.util.Date

class Contact(
    var name: String,
    var email: String,
    var phone: String,
    var birthday: Date?=null,
    var picture: String?=null
) : Serializable

data class MeetingPoint(
    val latitude: Double,
    val longitude: Double,
    val date: Date
) : Serializable