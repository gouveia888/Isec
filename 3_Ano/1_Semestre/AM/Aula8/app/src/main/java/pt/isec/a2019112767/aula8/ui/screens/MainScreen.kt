package pt.isec.a2019112767.aula8.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.currentBackStackEntryAsState
import pt.isec.a2019112767.aula8.ui.viewmodels.ContactsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: ContactsViewModel,
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
    ) {

    val currentScreen by navController.currentBackStackEntryAsState()
    var showExpanded by remember { mutableStateOf(false) }
    val route = currentScreen?.destination?.route

    Log.i("MainScreen", "Recomposing. Current route is: '$route'")

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Contacts") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0, 224, 255),
                    titleContentColor = Color(0, 0, 128),
                ),
                navigationIcon = {
                    if (currentScreen?.destination?.route != "list" && currentScreen?.destination?.route != null) {
                        IconButton(
                            onClick = { navController.navigateUp() }
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                    Log.i("MainScreen",
                        "currentScreen:${currentScreen?.destination?.route}")
                },
                actions = {
                    when (currentScreen?.destination?.route) {
                        "list" -> {
                            IconButton(onClick = { }) {
                                Icon(
                                    Icons.AutoMirrored.Filled.List,
                                    contentDescription = "Expand/Collapse"
                                )
                            }
                            IconButton(
                                onClick = { viewModel.selectContact(null)
                                            viewModel.prepareForEdit(null)
                                            navController.navigate("edit")
                                }
                            ) {
                                Icon(
                                    Icons.Filled.Add,
                                    contentDescription = "Add Contact"
                                )
                            }

                        }
                        "show" -> {
                            IconButton(
                                onClick = { navController.navigate("edit") }
                            ) {
                                Icon(
                                    Icons.Filled.Edit,
                                    contentDescription = "Edit Contact"
                                )
                            }

                        }
                        "edit" -> {
                            IconButton(
                                onClick = { viewModel.saveEditedContact(null)
                                            navController.popBackStack("list", inclusive = false)
                                }
                            ) {
                                Icon(
                                    Icons.Filled.Done,
                                    contentDescription = "Save Contact"
                                )
                            }
                        }
                    }
                },
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "list",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("list") {
                ListScreen(
                    contacts = viewModel.contactsList.getContacts(),
                    showExpanded = showExpanded,
                    onSelectContact = { contact ->
                        viewModel.selectContact(contact)
                        navController.navigate("show")
                    }
                )
            }
            composable("show") {
                viewModel.currentContact?.let { contact ->
                    ShowScreen(contact = contact)
                } ?: Text("No contact selected")
            }
            composable("edit") {
                EditScreen(
                    viewModel = viewModel
                )
            }
        }
    }
}
