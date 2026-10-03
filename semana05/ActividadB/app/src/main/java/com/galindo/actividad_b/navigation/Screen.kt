package com.tecsupfit.app.navigation

sealed class Screen(val route: String) {
    object Inicio : Screen("inicio")
    object Detalle : Screen("detalle/{claseId}") {
        fun createRoute(claseId: Int) = "detalle/$claseId"
    }
    object Confirmacion : Screen("confirmacion")
    object Reservas : Screen("reservas")
    object Rutinas : Screen("rutinas")
    object Perfil : Screen("perfil")
}
