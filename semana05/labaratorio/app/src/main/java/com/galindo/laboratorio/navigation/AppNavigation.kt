package com.galindo.laboratorio.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.galindo.laboratorio.screens.AcademicRecordScreen
import com.galindo.laboratorio.screens.DirectoryScreen
import com.galindo.laboratorio.screens.HomeScreen
import com.galindo.laboratorio.screens.LoginScreen
import com.galindo.laboratorio.screens.ProfileConfigScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(Screen.Directory.route) {
            DirectoryScreen(navController = navController)
        }
        composable(Screen.ProfileConfig.route) {
            ProfileConfigScreen(navController = navController)
        }
        composable(
            route = Screen.AcademicRecord.route,
            arguments = listOf(
                navArgument("studentId") {
                    type = NavType.StringType
                    defaultValue = "2024-0001"
                }
            )
        ) { backStackEntry ->
            val studentId = backStackEntry.arguments?.getString("studentId") ?: "2024-0001"
            AcademicRecordScreen(navController = navController, studentId = studentId)
        }
    }
}
