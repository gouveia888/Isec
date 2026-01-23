package pt.isec.safetysec.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import pt.isec.safetysec.services.FirebaseService
import pt.isec.safetysec.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RulesConfigScreen(navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    val userId = FirebaseService.getCurrentUserId() ?: ""
    val context = LocalContext.current

    // Estados para Limites (Requisitos: Velocidade e Inatividade)
    var speedLimit by remember { mutableStateOf("120") }
    var inactivityMins by remember { mutableStateOf("30") }

    // Estados para Janelas Temporais (Requisito: Privacidade/Horários)
    var startTime by remember { mutableStateOf("08:00") }
    var endTime by remember { mutableStateOf("20:00") }
    val daysOfWeek = stringArrayResource(id = R.array.days_array).toList()
    var selectedDays by remember { mutableStateOf(setOf("Segunda", "Terça", "Quarta", "Quinta", "Sexta")) }

    // Carregar dados existentes
    LaunchedEffect(userId) {
        if (userId.isNotEmpty()) {
            db.collection("users").document(userId).get().addOnSuccessListener { doc ->
                val rules = doc.get("monitoringRules") as? Map<String, Any>
                rules?.let {
                    speedLimit = it["speedLimit"]?.toString() ?: "120"
                    inactivityMins = it["inactivityMins"]?.toString() ?: "30"
                    val window = it["authWindow"] as? Map<String, Any>
                    window?.let { w ->
                        startTime = w["start"]?.toString() ?: "08:00"
                        endTime = w["end"]?.toString() ?: "20:00"
                        val days = w["days"] as? List<String>
                        if (days != null) selectedDays = days.toSet()
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(id=R.string.rules_config_title)) })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(stringResource(id=R.string.monitoring_params), style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)

            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = speedLimit,
                        onValueChange = { speedLimit = it },
                        label = { Text(stringResource(id=R.string.max_speed_label)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = inactivityMins,
                        onValueChange = { inactivityMins = it },
                        label = { Text(stringResource(id=R.string.inactivity_time_label)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(stringResource(id=R.string.auth_window_title), style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)

            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(stringResource(id=R.string.working_hours), fontWeight = FontWeight.Bold)
                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(value = startTime, onValueChange = { startTime = it }, label = { Text(stringResource(id=R.string.start_time)) }, modifier = Modifier.weight(1f))
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedTextField(value = endTime, onValueChange = { endTime = it }, label = { Text(stringResource(id=R.string.end_time)) }, modifier = Modifier.weight(1f))
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(stringResource(id=R.string.days_of_week_title), fontWeight = FontWeight.Bold)

                    //lista dos dias da semana
                    daysOfWeek.forEach { day ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Checkbox(
                                checked = selectedDays.contains(day),
                                onCheckedChange = {
                                    selectedDays = if (it) selectedDays + day else selectedDays - day
                                }
                            )
                            Text(day)
                        }
                    }
                }
            }

            Button(
                onClick = {
                    val rulesData = hashMapOf(
                        "speedLimit" to (speedLimit.toDoubleOrNull() ?: 120.0),
                        "inactivityMins" to (inactivityMins.toIntOrNull() ?: 30),
                        "authWindow" to hashMapOf(
                            "days" to selectedDays.toList(),
                            "start" to startTime,
                            "end" to endTime
                        )
                    )

                    db.collection("users").document(userId).update("monitoringRules", rulesData)
                        .addOnSuccessListener {
                            Toast.makeText(context, "Regras guardadas com sucesso!", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        }
                },
                modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp)
            ) {
                Icon(Icons.Default.Check, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(id=R.string.save_changes_btn))
            }
        }
    }
}