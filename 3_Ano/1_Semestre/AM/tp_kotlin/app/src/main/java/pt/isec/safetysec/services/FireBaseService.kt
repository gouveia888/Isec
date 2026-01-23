package pt.isec.safetysec.services

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import java.io.File


object FirebaseService {
    private val auth by lazy { FirebaseAuth.getInstance() }
    private val db by lazy { FirebaseFirestore.getInstance() }
    private val storage : StorageReference by lazy { Firebase.storage.reference }

    val currentUser: com.google.firebase.auth.FirebaseUser?
        get() = auth.currentUser

    fun signInWithEmail(email: String, password: String, onResult: (Throwable?) -> Unit) {
        try {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { result ->
                    onResult(result.exception)
                }
        } catch (e: Exception) {
            onResult(e) // Devolve o erro em vez de crashar a app
        }
    }

    fun getTempFilename(context: Context, prefix: String = "alerta", suffix: String = ".mp4"): String {
        return File.createTempFile(
            prefix, suffix,
            context.externalCacheDir
        ).absolutePath
    }

    fun listenToActiveAlerts(onAlertsChanged: (List<Map<String, Any>>) -> Unit) {
        try {
            db.collection("alerts")
                .whereEqualTo("status", "ACTIVE")
                .addSnapshotListener { snapshot, error ->
                    if (error != null) return@addSnapshotListener
                    val alerts = snapshot?.documents?.mapNotNull { it.data } ?: emptyList()
                    onAlertsChanged(alerts)
                }
        } catch (e: Exception) {
            Log.e("FirebaseService", "Erro ao inicializar listener: ${e.message}")
        }
    }

    fun updateOTPCode(userId: String, code: String) {
        db.collection("users").document(userId)
            .update("otpCode", code)
    }

    fun getCurrentUserId(): String? {
        return try {
            auth.currentUser?.uid
        } catch (e: Exception) {
            null
        }
    }

    fun registerUser(name: String, email: String, pass: String, role: String, otpCode: String, onResult: (Throwable?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid ?: ""
                    val userData = hashMapOf(
                        "uid" to uid,
                        "name" to name,
                        "email" to email,
                        "role" to role, // "Monitor" ou "Protegido"
                        "monitors" to listOf<String>(), // Lista vazia para começar
                        "otpCode" to otpCode
                    )
                    // Guarda os dados extra no Firestore
                    db.collection("users").document(uid).set(userData)
                        .addOnCompleteListener { result -> onResult(result.exception) }
                } else {
                    onResult(task.exception)
                }
            }
    }

    fun signOut() = auth.signOut()

    fun associateProtectedByOTP(otpCode: String, monitorId: String, onComplete: (Boolean, String?) -> Unit) {
        db.collection("users")
            .whereEqualTo("otpCode", otpCode) // Procura quem tem este código
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val protectedDoc = querySnapshot.documents[0]
                    val protectedId = protectedDoc.id
                    val protectedName = protectedDoc.getString("name") ?: "Utilizador Protegido"

                    //Adiciona o ID do Monitor à lista de monitores do Protegido
                    //Limpa o otpCode para que não possa ser usado outra vez
                    db.collection("users").document(protectedId)
                        .update(
                            "monitors", com.google.firebase.firestore.FieldValue.arrayUnion(monitorId),
                            "otpCode", null
                        )
                        .addOnSuccessListener {
                            onComplete(true, protectedName)
                        }
                        .addOnFailureListener {
                            onComplete(false, null)
                        }
                } else {
                    onComplete(false, null) // Código não encontrado ou expirado
                }
            }
            .addOnFailureListener {
                onComplete(false, null)
            }
    }

    fun listenToMyProtectedUsers(monitorId: String, onUsersChanged: (List<Map<String, Any>>) -> Unit) {
        db.collection("users")
            .whereArrayContains("monitors", monitorId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener
                val users = snapshot?.documents?.mapNotNull { it.data } ?: emptyList()
                onUsersChanged(users)
            }
    }

    fun removeMonitor(protectedId: String, monitorId: String, onResult: (Boolean) -> Unit) {
        db.collection("users").document(protectedId)
            .update("monitors", com.google.firebase.firestore.FieldValue.arrayRemove(monitorId))
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    fun uploadVideoOnly(alertId: String, videoFilePath: String, onComplete: (Boolean) -> Unit) {
        val file = File(videoFilePath)
        if (!file.exists()) {
            Log.e("FirebaseService", "Ficheiro de vídeo não encontrado para update: $videoFilePath")
            onComplete(false)
            return
        }

        val fileUri = Uri.fromFile(file)
        val videoRef = storage.child("alerts/videos/$alertId.mp4")

        //Upload do vídeo para o Storage
        videoRef.putFile(fileUri)
            .addOnSuccessListener {
                //obter o URL
                videoRef.downloadUrl.addOnSuccessListener { downloadUri ->

                    //update apenas o campo videoUrl
                    db.collection("alerts").document(alertId)
                        .update("videoUrl", downloadUri.toString())
                        .addOnSuccessListener {
                            Log.d("FirebaseService", "Campo videoUrl atualizado com sucesso!")
                            onComplete(true)
                        }
                        .addOnFailureListener { onComplete(false) }
                }
            }
            .addOnFailureListener {
                Log.e("FirebaseService", "Falha no upload do vídeo de update: ${it.message}")
                onComplete(false)
            }
    }

    fun listenToAlertsByProtectedIds(protectedIds: List<String>, onUpdate: (List<Map<String, Any>>) -> Unit) {
        val db = FirebaseFirestore.getInstance()

        db.collection("alerts")
            .whereIn("protectedId", protectedIds)
            .whereEqualTo("status", "ACTIVE")
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener

                val alerts = snapshot?.documents?.mapNotNull { it.data } ?: emptyList()
                onUpdate(alerts)
            }
    }
}