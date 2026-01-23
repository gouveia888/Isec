package pt.isec.safetysec.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import pt.isec.safetysec.R

@Composable
fun ProfileScreen(navController: NavController) {
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    val user = FirebaseAuth.getInstance().currentUser
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf(user?.email ?: "") }
    var securityCode by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }

    //carregar dados atuais
    LaunchedEffect(user?.uid) {
        user?.uid?.let { uid ->
            db.collection("users").document(uid).get().addOnSuccessListener { doc ->
                name = doc.getString("name") ?: ""
                securityCode = doc.getString("securityCode") ?: ""
            }
        }
    }

    Column(modifier = Modifier.padding(16.dp).fillMaxSize()) {
        Text(stringResource(id=R.string.my_profile_title), style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nome") })
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email (Apenas leitura)") }, readOnly = true)

        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

        Text(stringResource(id=R.string.security_section), style = MaterialTheme.typography.titleMedium)
        OutlinedTextField(
            value = securityCode,
            onValueChange = { if (it.length <= 4) securityCode = it },
            label = { Text(stringResource(id=R.string.security_code_label)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        OutlinedTextField(
            value = newPassword,
            onValueChange = { newPassword = it },
            label = { Text(stringResource(id=R.string.new_password_placeholder)) },
            visualTransformation = PasswordVisualTransformation()
        )

        Button(
            onClick = {
                val uid = user?.uid ?: return@Button
                val updates = hashMapOf(
                    "name" to name,
                    "securityCode" to securityCode
                )

                db.collection("users").document(uid).update(updates as Map<String, Any>)
                    .addOnSuccessListener {
                        if (newPassword.isNotEmpty()) {
                            user.updatePassword(newPassword)
                        }
                        Toast.makeText(context, "Perfil atualizado!", Toast.LENGTH_SHORT).show()
                    }
            },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        ) {
            Text(stringResource(id=R.string.save_changes_btn))
        }
    }
}