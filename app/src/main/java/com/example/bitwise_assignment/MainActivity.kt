package com.example.bitwise_assignment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
import androidx.compose.foundation.shape.RoundedCornerShape

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppNavigation()
        }
    }
}

data class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem("home", "Home", Icons.Default.Home),
    BottomNavItem("about", "About", Icons.Default.Info),
    BottomNavItem("explore", "Explore", Icons.Default.Explore),
    BottomNavItem("settings", "Settings", Icons.Default.Settings)
)

@Composable
fun AppNavigation() {
    var isDarkTheme by remember { mutableStateOf(false) }

    CompositionLocalProvider(LocalDarkTheme provides isDarkTheme) {
        Bitwise_assignmentTheme(darkTheme = isDarkTheme) {
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            val showBottomBar = currentRoute != null && 
                currentRoute != "quiz" && 
                currentRoute != "kuesioner" &&
                !currentRoute.startsWith("quiz/segment")

            Scaffold(
                containerColor = BgColor,
                bottomBar = {
                    if (showBottomBar) {
                        NavigationBar(
                            containerColor = if (isDarkTheme) Color(0xFF21132C) else Color.White,
                            contentColor = TextDark
                        ) {
                            bottomNavItems.forEach { item ->
                                val selected = when (item.route) {
                                    "home" -> currentRoute == "home" || currentRoute.startsWith("profile/")
                                    "about" -> currentRoute == "about"
                                    "explore" -> currentRoute == "explore" || currentRoute.startsWith("explore/")
                                    "settings" -> currentRoute == "settings" || currentRoute == "quiz/admin"
                                    else -> currentRoute == item.route
                                }
                                NavigationBarItem(
                                    icon = { Icon(item.icon, contentDescription = item.title) },
                                    label = { Text(item.title, fontWeight = FontWeight.SemiBold) },
                                    selected = selected,
                                    onClick = {
                                        if (!selected) {
                                            navController.navigate(item.route) {
                                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = Color.White,
                                        selectedTextColor = Purple,
                                        unselectedIconColor = MutedText,
                                        unselectedTextColor = MutedText,
                                        indicatorColor = Purple
                                    )
                                )
                            }
                        }
                    }
                }
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = "home",
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable("home") {
                        HomeScreen(
                            onMemberClick = { id -> navController.navigate("profile/$id") }
                        )
                    }
                    
                    composable("about") { PlaceholderScreen("About Screen") }
                    
                    composable("explore") { 
                        ProjectsScreen(
                            onLearningClick = { navController.navigate("explore/learning") },
                            onQuizClick = { navController.navigate("quiz") },
                            onQuestionnaireClick = { navController.navigate("kuesioner") }
                        ) 
                    }

                    composable("explore/learning") {
                        LearningMenuScreen(
                            onBranchingClick = { navController.navigate("explore/branching") },
                            onGeometryClick = { navController.navigate("explore/geometry") },
                            onBack = { navController.popBackStack() }
                        )
                    }

                    composable("explore/branching") {
                        BranchingScreen(
                            onBack = { navController.popBackStack() }
                        )
                    }

                    composable("explore/geometry") {
                        GeometryFormulasScreen(
                            onBack = { navController.popBackStack() }
                        )
                    }

                    composable("kuesioner") {
                        QuestionnaireScreen(
                            onBack = { navController.popBackStack() }
                        )
                    }

                    composable("quiz") {
                        QuizMenuScreen(
                            onSegmentClick = { segmentId -> navController.navigate("quiz/segment/$segmentId") },
                            onBack = { navController.popBackStack() }
                        )
                    }

                    composable(
                        route = "quiz/segment/{segmentId}",
                        arguments = listOf(navArgument("segmentId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val segmentId = backStackEntry.arguments?.getInt("segmentId") ?: 1
                        QuizPlayScreen(
                            segmentId = segmentId,
                            onBack = { navController.popBackStack() }
                        )
                    }

                    composable("quiz/admin") {
                        QuizAdminScreen(
                            onBack = { navController.popBackStack() }
                        )
                    }

                    composable("settings") { 
                        MenuScreen(
                            isDarkTheme = isDarkTheme,
                            onToggleTheme = { isDarkTheme = it },
                            onAdminClick = { navController.navigate("quiz/admin") }
                        ) 
                    }

                    composable(
                        route = "profile/{id}",
                        arguments = listOf(navArgument("id") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val id = backStackEntry.arguments?.getInt("id") ?: 0
                        ProfileScreen(
                            memberId = id,
                            onBack = { navController.popBackStack() }
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
    onAdminClick : () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor)
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Dark Mode", fontSize = 18.sp, color = TextDark, fontWeight = FontWeight.Bold)
            Switch(
                checked = isDarkTheme,
                onCheckedChange = onToggleTheme,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = if (isDarkTheme) Color(0xFFFFF9FF) else Color.White,
                    checkedTrackColor = Purple,
                    uncheckedThumbColor = if (isDarkTheme) Color(0xFF9A8BB5) else Color.Gray,
                    uncheckedTrackColor = if (isDarkTheme) Color(0xFF21132C) else Color(0xFFE5E7EB)
                )
            )
        }
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = onAdminClick,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Purple),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Statistik", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun PlaceholderScreen(title: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor)
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(title, style = MaterialTheme.typography.headlineMedium, color = TextDark)
        }
    }
}
