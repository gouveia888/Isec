package pt.isec.safetysec.models

class User (
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val profileType: String = "BOTH", // MONITOR, PROTECTED, ou BOTH [cite: 16]
    val cancelCode: String = "", // Código para cancelar alertas [cite: 32]
    val monitorIds: List<String> = emptyList(), // Lista de monitores autorizados [cite: 45]
    val protectedIds: List<String> = emptyList() // Lista de protegidos a cargo [cite: 59]
){

}