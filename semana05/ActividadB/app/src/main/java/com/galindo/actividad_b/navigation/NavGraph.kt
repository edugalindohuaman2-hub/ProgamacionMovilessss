package com.tecsupfit.app.navigation

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsupfit.app.data.SampleData
import com.tecsupfit.app.model.Reserva
import com.tecsupfit.app.ui.components.TecsupFitBottomBar
import com.tecsupfit.app.ui.screens.*
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TecsupFitApp() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    // Estado compartido a nivel de app (SIN ViewModel / MVVM)
    var reservas by remember { mutableStateOf(listOf<Reserva>()) }

    val rutasConBottomBar = listOf(
        Screen.Inicio.route, Screen.Reservas.route, Screen.Rutinas.route, Screen.Perfil.route
    )

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(tituloPara(currentRoute)) })
        },
        bottomBar = {
            if (currentRoute in rutasConBottomBar) {
                TecsupFitBottomBar(navController = navController, currentRoute = currentRoute)
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Inicio.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screen.Inicio.route) {
                InicioScreen(
                    clases = SampleData.clases,
                    onClaseClick = { clase ->
                        navController.navigate(Screen.Detalle.createRoute(clase.id))
                    }
                )
            }

            composable(
                route = Screen.Detalle.route,
                arguments = listOf(navArgument("claseId") { type = NavType.IntType })
            ) { entry ->
                val claseId = entry.arguments?.getInt("claseId") ?: -1
                val clase = SampleData.clases.find { it.id == claseId }
                if (clase != null) {
                    DetalleClaseScreen(
                        clase = clase,
                        onReservar = { horarioElegido ->
                            reservas = reservas + Reserva(clase, horarioElegido)
                            navController.navigate(Screen.Confirmacion.route)
                        }
                    )
                }
            }

            composable(Screen.Confirmacion.route) {
                ConfirmacionScreen(
                    reserva = reservas.lastOrNull(),
                    onVerReservas = {
                        navController.navigate(Screen.Reservas.route) {
                            popUpTo(Screen.Inicio.route)
                        }
                    }
                )
            }

            composable(Screen.Reservas.route) {
                ReservasScreen(reservas = reservas)
            }

            composable(Screen.Rutinas.route) {
                RutinasScreen()
            }

            composable(Screen.Perfil.route) {
                PerfilScreen(totalReservas = reservas.size)
            }
        }
    }
}

private fun tituloPara(route: String?): String = when {
    route == Screen.Inicio.route -> "TECSUP Fit"
    route == Screen.Reservas.route -> "Mis Reservas"
    route == Screen.Rutinas.route -> "Rutinas"
    route == Screen.Perfil.route -> "Perfil"
    route == Screen.Confirmacion.route -> "Confirmación"
    route?.startsWith("detalle") == true -> "Detalle de clase"
    else -> "TECSUP Fit"
}
