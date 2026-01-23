package pt.isec.safetysec.models

enum class RuleType {
    FALL, ACCIDENT, GEOFENCING, SPEED_CONTROL, INACTIVITY, PANIC_BUTTON
}

data class MonitoringRule(
    val id: String = "",
    val type: RuleType,
    val isEnabled: Boolean = false, // Autorizada pelo Protegido [cite: 23]
    val maxValue: Double? = null, // Para velocidade ou duração
    val latitude: Double? = null, // Para Geofencing
    val longitude: Double? = null, // Para Geofencing
    val radius: Double? = null // Para Geofencing (em metros)
)