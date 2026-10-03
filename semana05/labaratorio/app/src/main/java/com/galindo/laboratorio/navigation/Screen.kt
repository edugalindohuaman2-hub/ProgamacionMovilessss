package com.galindo.laboratorio.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object Directory : Screen("directory")
    object ProfileConfig : Screen("profile_config")
    object AcademicRecord : Screen("academic_record/{studentId}") {
        fun createRoute(studentId: String): String = "academic_record/$studentId"
    }
}
