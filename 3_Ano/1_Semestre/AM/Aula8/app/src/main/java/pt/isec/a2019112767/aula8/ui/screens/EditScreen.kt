package pt.isec.a2019112767.aula8.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pt.isec.a2019112767.aula8.ui.viewmodels.ContactsViewModel

@Composable
fun EditScreen(
    viewModel: ContactsViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Campo Nome: Usa o estado temporário do ViewModel
        OutlinedTextField(
            value = viewModel.tempName,
            onValueChange = { viewModel.tempName = it }, // Atualiza o estado
            label = { Text("Nome:") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            isError = viewModel.tempName.isEmpty()
        )

        // Campo E-mail
        OutlinedTextField(
            value = viewModel.tempEmail,
            onValueChange = { viewModel.tempEmail = it },
            label = { Text("E-mail:") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        )

        // Campo Telefone
        OutlinedTextField(
            value = viewModel.tempPhone,
            onValueChange = { viewModel.tempPhone = it },
            label = { Text("Telefone:") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        )

        // Nota sobre Birthday:
        // A implementação da UI para a data é omitida por ser complexa (DatePicker).
        // A lógica de salvamento no ViewModel (saveEditedContact) já está pronta para receber a data.
        Text(text = "Birthday (Data de Nascimento): Implementar DatePicker", modifier = Modifier.padding(top = 16.dp))
    }
}