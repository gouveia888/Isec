package pt.isec.safetysec

import AlertMapScreen
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.FirebaseApp
import pt.isec.safetysec.services.LocationForegroundService
import pt.isec.safetysec.ui.screens.*
import pt.isec.safetysec.ui.theme.SafetYSecTheme
import androidx.navigation.NavType
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import pt.isec.safetysec.services.FirebaseService

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        // Verifica se todas foram aceites (opcional para log)
        val allGranted = permissions.values.all { it }
        if (allGranted) {
            Log.d("SafetYSec", "Todas as permissões (GPS, Cam, Mic) concedidas.")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        try {
            FirebaseApp.initializeApp(this)
        } catch (e: Exception) {
            Log.e("SafetYSec", "Erro ao inicializar Firebase: ${e.message}")
        }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val requiredPermissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        )

        val missingPermissions = requiredPermissions.filter {
            checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED
        }

        if (missingPermissions.isNotEmpty()) {
            requestPermissionLauncher.launch(missingPermissions.toTypedArray())
        }

        val intent = Intent(this, LocationForegroundService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }

        setContent {
            SafetYSecTheme {
                SafetYSecApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SafetYSecApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.app_name)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                actions = {
                    if(currentRoute != "login" && currentRoute != "register")
                        IconButton(onClick = {
                            FirebaseService.signOut()
                            navController.navigate("login") {
                                popUpTo(0) { inclusive = true }
                            }
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                contentDescription = "Logout",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("register") {
                RegisterScreen(navController = navController)
            }
            composable("login") {
                LoginScreen(navController = navController)
            }
            composable("profile") {
                ProfileScreen(navController = navController)
            }
            composable("dashboard_monitor") {
                MonitorDashboardScreen(navController = navController)
            }
            composable("dashboard_protected") {
                ProtectedDashboardScreen(navController = navController)
            }
            composable("geofencing/{protectedId}") { backStackEntry ->
                val protectedId = backStackEntry.arguments?.getString("protectedId") ?: ""
                GeofencingScreen(protectedId = protectedId, navController = navController)
            }
            composable("rules_config") {
                RulesConfigScreen(navController = navController)
            }
            composable(
                route = "alert_map/{protectedId}/{lat}/{lng}",
                arguments = listOf(
                    navArgument("protectedId") { type = NavType.StringType },
                    navArgument("lat") { type = NavType.FloatType },
                    navArgument("lng") { type = NavType.FloatType }
                )
            ) { backStackEntry ->

                val protectedId = backStackEntry.arguments?.getString("protectedId") ?: ""
                val lat = backStackEntry.arguments?.getFloat("lat")?.toDouble() ?: 0.0
                val lng = backStackEntry.arguments?.getFloat("lng")?.toDouble() ?: 0.0

                AlertMapScreen(protectedId, lat, lng)
            }
        }
    }
}