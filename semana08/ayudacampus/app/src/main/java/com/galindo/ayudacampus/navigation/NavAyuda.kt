package com.galindo.ayudacampus.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.galindo.ayudacampus.AyudaApp
import com.galindo.ayudacampus.data.model.Rol
import com.galindo.ayudacampus.data.prefs.PreferenciasStore
import com.galindo.ayudacampus.ui.cliente.LoginScreen
import com.galindo.ayudacampus.ui.cliente.MisTicketsScreen
import com.galindo.ayudacampus.ui.cliente.NuevoTicketScreen
import com.galindo.ayudacampus.ui.soporte.PanelScreen
import com.galindo.ayudacampus.ui.splash.SplashScreen
import com.galindo.ayudacampus.viewmodel.MisTicketsViewModel
import com.galindo.ayudacampus.viewmodel.NuevoTicketViewModel
import com.galindo.ayudacampus.viewmodel.SesionViewModel

object NavDestinations {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val CLIENTE_MAIN = "cliente_main"
    const val SOPORTE_MAIN = "soporte_main"
    const val NUEVO_TICKET = "nuevo_ticket"
    const val DETALLE_TICKET = "detalle_ticket"
}

@Composable
fun NavAyuda(
    sesionViewModel: SesionViewModel,
    preferenciasStore: PreferenciasStore,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current
    val app = context.applicationContext as AyudaApp

    NavHost(
        navController = navController,
        startDestination = NavDestinations.SPLASH,
        modifier = modifier
    ) {
        composable(NavDestinations.SPLASH) {
            SplashScreen(
                preferenciasStore = preferenciasStore,
                onNavigateNext = { rolGuardado ->
                    when (rolGuardado) {
                        Rol.CLIENTE -> {
                            navController.navigate(NavDestinations.CLIENTE_MAIN) {
                                popUpTo(NavDestinations.SPLASH) { inclusive = true }
                            }
                        }
                        Rol.ADMIN -> {
                            navController.navigate(NavDestinations.SOPORTE_MAIN) {
                                popUpTo(NavDestinations.SPLASH) { inclusive = true }
                            }
                        }
                        null -> {
                            navController.navigate(NavDestinations.LOGIN) {
                                popUpTo(NavDestinations.SPLASH) { inclusive = true }
                            }
                        }
                    }
                }
            )
        }

        composable(NavDestinations.LOGIN) {
            LoginScreen(
                viewModel = sesionViewModel,
                onLoginSuccess = { rol ->
                    val destination = if (rol == Rol.ADMIN) NavDestinations.SOPORTE_MAIN else NavDestinations.CLIENTE_MAIN
                    navController.navigate(destination) {
                        popUpTo(NavDestinations.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(NavDestinations.CLIENTE_MAIN) {
            val usuarioActual by sesionViewModel.usuarioActual.collectAsState()
            val clienteId = usuarioActual?.id ?: 1

            val misTicketsViewModel: MisTicketsViewModel = viewModel(
                factory = app.misTicketsViewModelFactory(clienteId)
            )

            MisTicketsScreen(
                viewModel = misTicketsViewModel,
                onNuevoTicketClick = {
                    navController.navigate(NavDestinations.NUEVO_TICKET)
                },
                onTicketClick = { ticketId ->
                    navController.navigate("${NavDestinations.DETALLE_TICKET}/$ticketId")
                }
            )
        }

        composable(NavDestinations.NUEVO_TICKET) {
            val usuarioActual by sesionViewModel.usuarioActual.collectAsState()
            val clienteId = usuarioActual?.id ?: 1

            val nuevoTicketViewModel: NuevoTicketViewModel = viewModel(
                factory = app.nuevoTicketViewModelFactory(clienteId)
            )

            NuevoTicketScreen(
                viewModel = nuevoTicketViewModel,
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(NavDestinations.SOPORTE_MAIN) {
            PanelScreen()
        }
    }
}
