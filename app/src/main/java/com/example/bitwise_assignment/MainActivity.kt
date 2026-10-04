package com.example.bitwise_assignment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bitwise_assignment.ui.theme.Bitwise_assignmentTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppNavigation()
        }
    }
}

@Composable
fun AppNavigation() {
    var isDarkTheme by remember { mutableStateOf(false) }

    CompositionLocalProvider(LocalDarkTheme provides isDarkTheme) {
        Bitwise_assignmentTheme(darkTheme = isDarkTheme) {
            val navController = rememberNavController()
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(drawerContainerColor = BgColor) {
                    Spacer(Modifier.height(16.dp))
                    NavigationDrawerItem(
                        label = { Text("Home", color = TextDark) },
                        selected = currentRoute == "home",
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate("home") {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                        colors = NavigationDrawerItemDefaults.colors(selectedContainerColor = PurpleSoft)
                    )
                    NavigationDrawerItem(
                        label = { Text("About", color = TextDark) },
                        selected = currentRoute == "about",
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate("about") {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                        colors = NavigationDrawerItemDefaults.colors(selectedContainerColor = PurpleSoft)
                    )
                    NavigationDrawerItem(
                        label = { Text("Projects", color = TextDark) },
                        selected = currentRoute == "projects",
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate("projects") {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                        colors = NavigationDrawerItemDefaults.colors(selectedContainerColor = PurpleSoft)
                    )
                    NavigationDrawerItem(
                        label = { Text("Settings", color = TextDark) },
                        selected = currentRoute == "settings",
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate("settings") {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                        colors = NavigationDrawerItemDefaults.colors(selectedContainerColor = PurpleSoft)
                    )
                }
            }
        ) {
            NavHost(navController = navController, startDestination = "home") {

                composable("home") {
                    HomeScreen(
                        onMemberClick = { id -> navController.navigate("profile/$id") },
                        onOpenDrawer = { scope.launch { drawerState.open() } }
                    )
                }
                
                composable("about") { PlaceholderScreen("About Screen", onOpenDrawer = { scope.launch { drawerState.open() } }) }
                composable("projects") { PlaceholderScreen("Projects Screen", onOpenDrawer = { scope.launch { drawerState.open() } }) }
                
                composable("settings") { 
                    MenuScreen(
                        isDarkTheme = isDarkTheme,
                        onToggleTheme = { isDarkTheme = it },
                        onOpenDrawer = { scope.launch { drawerState.open() } }
                    ) 
                }

                composable(
                    route = "profile/{id}",
                    arguments = listOf(navArgument("id") { type = NavType.IntType })
                ) { backStackEntry ->
                    val id = backStackEntry.arguments?.getInt("id") ?: 0
                    ProfileScreen(
                        memberId = id,
                        onBack = { navController.popBackStack() },
                        onOpenDrawer = { scope.launch { drawerState.open() } }
                    )
                }
            }
        }
      }
    }
}

@Composable
fun MenuScreen(
    isDarkTheme: Boolean,
    onToggleTheme: (Boolean) -> Unit,
    onOpenDrawer: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor)
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        AppHeader(onOpenDrawer = onOpenDrawer)
        Spacer(Modifier.height(32.dp))
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Dark Mode", fontSize = 18.sp, color = TextDark, fontWeight = FontWeight.Bold)
            Switch(
                checked = isDarkTheme,
                onCheckedChange = onToggleTheme,
                colors = SwitchDefaults.colors(checkedThumbColor = Purple, checkedTrackColor = PurpleSoft)
            )
        }
    }
}

@Composable
fun PlaceholderScreen(title: String, onOpenDrawer: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor)
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        AppHeader(onOpenDrawer = onOpenDrawer)
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(title, style = MaterialTheme.typography.headlineMedium, color = TextDark)
        }
    }
}