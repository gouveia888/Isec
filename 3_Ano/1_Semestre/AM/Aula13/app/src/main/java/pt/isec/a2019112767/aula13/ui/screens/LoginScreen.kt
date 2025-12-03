package pt.isec.a2019112767.aula13.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/*Ecra de login
* deve ter duas caixas de login texto (email e password)
* e dois botoes  para efetuar o login
* */

@Composable
fun LoginScreen(
    viewModel: FirebaseViewModel,
    onSuccess: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val email = remember { mutableStateOf("")}
    val password = remember { mutableStateOf("")}

    LaunchedEffect(viewModel.user.value) {
        if (viewModel.user.value != null && viewModel.error.value == null) {
            onSuccess()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        if (viewModel.error.value != null) {
            Text(
                text="Error: ${viewModel.error.value}",
                modifier = Modifier
                    .background(Color(255, 0, 0))
                    .padding(16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        OutlinedTextField(
            value = email.value,
            onValueChange = {email.value = it},
            label = {Text(stringResource(R.string.email))},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = password.value,
            onValueChange = {password.value = it},
            label = {Text(stringResource(R.string.password))},
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            Button(onClick = {
                viewModel.createUserWithEmail(email.value, password.value)
            }) {
                Text(stringResource(R.string.register))
            }
            Button(onClick = {
                viewModel.signInWithEmail(email.value, password.value)
            }) {
                Text(stringResource(R.string.login))
            }
        }
    }
}
