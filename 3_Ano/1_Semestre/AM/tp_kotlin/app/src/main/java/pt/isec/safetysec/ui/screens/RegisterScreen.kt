package pt.isec.safetysec.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pt.isec.safetysec.services.FirebaseService
import pt.isec.safetysec.R

@Composable
fun RegisterScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isMonitor by remember { mutableStateOf(true) } // Default é Monitor
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var optcode by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = stringResource(id=R.string.register_title), style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(stringResource(id=R.string.full_name_label)) },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(stringResource(id=R.string.password_label)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Seleção de Papel (Monitor ou Protegido)
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(selected = isMonitor, onClick = { isMonitor = true })
            Text(stringResource(id=R.string.profile_monitor))
            Spacer(modifier = Modifier.width(16.dp))
            RadioButton(selected = !isMonitor, onClick = { isMonitor = false })
            Text(stringResource(id=R.string.profile_protected))
        }

        if (!isMonitor) {
            OutlinedTextField(
                value = optcode,
                onValueChange = { optcode = it },
                label = { Text(stringResource(id=R.string.security_code_label)) },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (errorMessage != null) {
            Text(text = errorMessage!!, color = Color.Red)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = {
                if (name.isNotEmpty() && email.isNotEmpty() && password.length >= 6) {
                    isLoading = true
                    val role = if (isMonitor) "Monitor" else "Protegido"

                    FirebaseService.registerUser(name, email, password, role,optcode) { error ->
                        isLoading = false
                        if (error == null) {
                            // Registo feito com sucesso, volta para o Login ou vai direto para o Dash
                            navController.navigate("login") {
                                popUpTo("register") { inclusive = true }
                            }
                        } else {
                            errorMessage = error.localizedMessage
                        }
                    }
                } else {
                    errorMessage = "Preencha todos os campos (Senha min. 6 caracteres)"
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            if (isLoading) CircularProgressIndicator()
            else Text(stringResource(id=R.string.register_btn))
        }

        TextButton(onClick = { navController.popBackStack() }) {
            Text(stringResource(id=R.string.already_have_account))
        }
    }
}