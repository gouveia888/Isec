package pt.isec.safetysec.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import pt.isec.safetysec.R
import pt.isec.safetysec.services.FirebaseService

@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    // Estados para o Segundo Fator (MFA)
    var showPinDialog by remember { mutableStateOf(false) }
    var introducedPin by remember { mutableStateOf("") }
    var correctPin by remember { mutableStateOf("") }
    var userRole by remember { mutableStateOf("") }

    val db = FirebaseFirestore.getInstance()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.login),
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(stringResource(id=R.string.password_label)) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    FirebaseService.signInWithEmail(email, password) { error ->
                        if (error == null) {
                            val uid = FirebaseService.getCurrentUserId() ?: ""

                            //procurar dados do utilizador
                            db.collection("users").document(uid).get()
                                .addOnSuccessListener { doc ->
                                    userRole = doc.getString("role") ?: "PROTECTED"
                                    //codigo MFA
                                    val savedPin = doc.getString("securityCode")

                                    if (!savedPin.isNullOrEmpty()) {
                                        correctPin = savedPin
                                        showPinDialog = true
                                    } else {
                                        navigateByRole(navController, userRole)
                                    }
                                }
                                .addOnFailureListener {
                                    Toast.makeText(context, "Erro ao carregar perfil.", Toast.LENGTH_SHORT).show()
                                }
                        } else {
                            Toast.makeText(context, "Erro: ${error.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(id = R.string.login))
        }

        TextButton(onClick = { navController.navigate("register") }) {
            Text(stringResource(id = R.string.textToRegister))
        }
    }

    //MFA
    if (showPinDialog) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text(stringResource(id=R.string.mfa_title)) },
            text = {
                Column {
                    Text(stringResource(id=R.string.mfa_description))
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = introducedPin,
                        onValueChange = { if (it.length <= 4) introducedPin = it },
                        label = { Text(stringResource(id=R.string.pin_label)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        visualTransformation = PasswordVisualTransformation()
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (introducedPin == correctPin) {
                        showPinDialog = false
                        navigateByRole(navController, userRole)
                    } else {
                        Toast.makeText(context, "PIN Incorreto!", Toast.LENGTH_SHORT).show()
                        Log.e("MFA", "PIN Incorreto")
                    }
                }) {
                    Text(stringResource(id=R.string.confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    FirebaseService.signOut()
                    showPinDialog = false
                }) {
                    Text(stringResource(id=R.string.cancel))
                }
            }
        )
    }
}

fun navigateByRole(navController: NavController, role: String) {
    val targetRoute = if (role.equals("Monitor", ignoreCase = true)) {
        "dashboard_monitor"
    } else {
        "dashboard_protected"
    }

    navController.navigate(targetRoute) {
        popUpTo("login") { inclusive = true }
    }
}