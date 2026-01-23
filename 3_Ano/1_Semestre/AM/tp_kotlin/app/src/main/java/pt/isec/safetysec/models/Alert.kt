package pt.isec.safetysec.models

data class Alert(
    val id: String = "",
    val protectedId: String = "",
    val protectedName: String = "",
    val type: RuleType,
    val timestamp: Long = System.currentTimeMillis(),
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val videoUrl: String? = null,
    val status: String = ""       // ACTIVE ou RESOLVED
)