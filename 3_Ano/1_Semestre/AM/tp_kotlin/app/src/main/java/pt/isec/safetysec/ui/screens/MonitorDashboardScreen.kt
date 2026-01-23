package pt.isec.safetysec.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pt.isec.safetysec.services.FirebaseService
import pt.isec.safetysec.R
import java.util.Date
import java.util.Locale.getDefault

@Composable
fun MonitorDashboardScreen(navController: NavController) {
    var activeAlerts by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var myProtectedUsers by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    val monitorId = FirebaseService.getCurrentUserId() ?: ""
    var showAssocDialog by remember { mutableStateOf(false) }
    var assocOtpCode by remember { mutableStateOf("") }
    val context = LocalContext.current
    var isProcessing by remember { mutableStateOf(false) }
    val myProtectedIds = remember(myProtectedUsers) {
        myProtectedUsers.mapNotNull { it["uid"] as? String }
    }

    // Listeners
    LaunchedEffect(Unit) {
        FirebaseService.listenToActiveAlerts { alerts -> activeAlerts = alerts }
    }
    LaunchedEffect(monitorId) {
        if (monitorId.isNotEmpty()) {
            FirebaseService.listenToMyProtectedUsers(monitorId) { users -> myProtectedUsers = users }
        }
    }

    LaunchedEffect(myProtectedIds) {
        if (myProtectedIds.isNotEmpty()) {
            FirebaseService.listenToAlertsByProtectedIds(myProtectedIds) { alerts ->
                activeAlerts = alerts
            }
        } else {
            activeAlerts = emptyList()
        }
    }

    //layout principal
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // titulo
        item {
            Row {
                Text(text = stringResource(id=R.string.monitor_panel_title), style = MaterialTheme.typography.headlineMedium)
                IconButton(onClick = { navController.navigate("profile") }) {
                    Icon(Icons.Default.Settings, contentDescription = "Editar Perfil")
                }
            }
            Button(
                onClick = { showAssocDialog = true },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ) { Text(stringResource(id=R.string.associate_new_protected)) }
        }


        // utilizadores monitorizados
        item {
            Text(text = stringResource(id=R.string.my_protected_users), style = MaterialTheme.typography.titleLarge)
        }

        if (myProtectedUsers.isEmpty()) {
            item { Text(stringResource(id=R.string.no_protected_users), color = Color.Gray) }
        } else {
            items(myProtectedUsers) { user ->
                ProtectedUserCard(
                    name = user["name"] as? String ?: "Sem Nome",
                    status = user["status"] as? String ?: "Ativo",
                    onDetailsClick = {
                        val pId = user["uid"] as? String ?: ""
                        navController.navigate("geofencing/$pId")
                    }
                )
            }
        }

        //alertas ativos
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = stringResource(id=R.string.recent_alerts), style = MaterialTheme.typography.titleLarge, color = Color.Red)
        }

        if (activeAlerts.isEmpty()) {
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Text(stringResource(id=R.string.no_critical_alerts), modifier = Modifier.padding(16.dp))
                }
            }
        } else {
            items(activeAlerts) { alert ->
                AlertItemCard(
                    alert = alert,
                    navController = navController
                )
            }
        }
    }
    if (showAssocDialog) {
        AlertDialog(
            onDismissRequest = { if (!isProcessing) showAssocDialog = false },
            title = { Text(stringResource(id=R.string.assoc_dialog_title)) },
            text = {
                Column {
                    Text(stringResource(id=R.string.assoc_dialog_desc))
                    OutlinedTextField(
                        value = assocOtpCode,
                        onValueChange = { if (it.length <= 6) assocOtpCode = it },
                        label = { Text(stringResource(id=R.string.assoc_dialog_desc)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isProcessing
                    )
                }
            },
            confirmButton = {
                Button(
                    enabled = assocOtpCode.length == 6 && !isProcessing,
                    onClick = {
                        isProcessing = true
                        FirebaseService.associateProtectedByOTP(assocOtpCode, monitorId) { success, errorMsg ->
                            isProcessing = false
                            if (success) {
                                showAssocDialog = false
                                assocOtpCode = ""
                                Toast.makeText(context, "Protegido associado!", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, errorMsg ?: "Código inválido", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                ) {
                    if (isProcessing) CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
                    else Text(stringResource(id=R.string.confirm))
                }
            },
            dismissButton = {
                TextButton(
                    enabled = !isProcessing,
                    onClick = { showAssocDialog = false }
                ) { Text(stringResource(id=R.string.cancel)) }
            }
        )
    }
}

@Composable
fun AlertItemCard(alert: Map<String, Any>,navController: NavController) {
    val timestamp = alert["timestamp"] as? Long ?: 0L
    val sdfDate = java.text.SimpleDateFormat("dd/MM/yyyy", getDefault())
    val sdfTime = java.text.SimpleDateFormat("HH:mm:ss", getDefault())
    val dateStr = sdfDate.format(Date(timestamp))
    val timeStr = sdfTime.format(Date(timestamp))

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.Red)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Warning, contentDescription = null, tint = Color.Red)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${alert["type"]}: ${alert["protectedName"]}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = stringResource(id = R.string.alert_date_label, dateStr),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = stringResource(id = R.string.alert_time_label, timeStr),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Text(
                text = stringResource(id = R.string.alert_gps_label, alert["location"] ?: ""),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )

            Button(
                onClick = {
                    val coords = alert["location"] as? String ?: "0.0,0.0"
                    val parts = coords.split(",")
                    if (parts.size == 2) {
                        val lat = parts[0].trim().toFloat()
                        val lng = parts[1].trim().toFloat()
                        val pId = alert["protectedId"] as? String ?: ""
                        navController.navigate("alert_map/$pId/$lat/$lng")
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text(stringResource(id=R.string.view_on_map))
            }
        }
    }
}

@Composable
fun ProtectedUserCard(name: String, status: String, onDetailsClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = name, style = MaterialTheme.typography.titleMedium)
                Text(text = stringResource(id = R.string.user_status_label, status), color = MaterialTheme.colorScheme.primary)
            }
            IconButton(onClick = onDetailsClick) {
                Icon(Icons.Default.Settings, contentDescription = stringResource(id = R.string.settings_label))
            }
        }
    }
}