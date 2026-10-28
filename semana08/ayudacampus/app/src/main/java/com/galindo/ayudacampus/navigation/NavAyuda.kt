package com.galindo.ayudacampus.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.galindo.ayudacampus.AyudaApp
import com.galindo.ayudacampus.data.model.Rol
import com.galindo.ayudacampus.data.prefs.PreferenciasStore
import com.galindo.ayudacampus.ui.cliente.AjustesScreen
import com.galindo.ayudacampus.ui.cliente.DetalleTicketScreen
import com.galindo.ayudacampus.ui.cliente.LoginScreen
import com.galindo.ayudacampus.ui.cliente.MisTicketsScreen
import com.galindo.ayudacampus.ui.cliente.NuevoTicketScreen
import com.galindo.ayudacampus.ui.cliente.PerfilScreen
import com.galindo.ayudacampus.ui.soporte.AtenderTicketScreen
import com.galindo.ayudacampus.ui.soporte.BandejaScreen
import com.galindo.ayudacampus.ui.soporte.FormularioCategoriaScreen
import com.galindo.ayudacampus.ui.soporte.PanelScreen
import com.galindo.ayudacampus.ui.splash.SplashScreen
import com.galindo.ayudacampus.viewmodel.AtenderViewModel
import com.galindo.ayudacampus.viewmodel.BandejaViewModel
import com.galindo.ayudacampus.viewmodel.CategoriasViewModel
import com.galindo.ayudacampus.viewmodel.MisTicketsViewModel
import com.galindo.ayudacampus.viewmodel.NuevoTicketViewModel
import com.galindo.ayudacampus.viewmodel.PanelViewModel
import com.galindo.ayudacampus.viewmodel.SesionViewModel
import com.galindo.ayudacampus.viewmodel.TicketDetalleViewModel

object Rutas {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val REGISTRO = "registro"
    const val MIS_TICKETS = "mis_tickets"
    const val NUEVO_TICKET = "nuevo_ticket"
    const val PERFIL = "perfil"
    const val DETALLE_TICKET = "detalle_ticket"

    const val PANEL = "panel"
    const val BANDEJA = "bandeja"
    const val CATEGORIAS = "categorias"
    const val AJUSTES = "ajustes"
    const val ATENDER_TICKET = "atender_ticket"
}

private data class ItemNavegacion(
    val titulo: String,
    val ruta: String,
    val icono: ImageVector
)

@Composable
fun NavAyuda(
    sesionViewModel: SesionViewModel,
    preferenciasStore: PreferenciasStore,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current
    val app = context.applicationContext as AyudaApp

    val usuarioActual by sesionViewModel.usuarioActual.collectAsState()
    val recibirTickets by sesionViewModel.recibirTickets.collectAsState()

    val rolActivo = usuarioActual?.rol?.let {
        runCatching { Rol.valueOf(it.uppercase()) }.getOrNull()
    }

    val currentDestination = navController.currentBackStackEntryAsState().value?.destination
    val currentRoute = currentDestination?.route

    val itemsCliente = listOf(
        ItemNavegacion("Tickets", Rutas.MIS_TICKETS, Icons.AutoMirrored.Filled.List),
        ItemNavegacion("Nuevo", Rutas.NUEVO_TICKET, Icons.Default.Add),
        ItemNavegacion("Perfil", Rutas.PERFIL, Icons.Default.Person)
    )

    val itemsSoporte = listOf(
        ItemNavegacion("Panel", Rutas.PANEL, Icons.Default.BarChart),
        ItemNavegacion("Tickets", Rutas.BANDEJA, Icons.AutoMirrored.Filled.List),
        ItemNavegacion("Categorías", Rutas.CATEGORIAS, Icons.Default.Tag),
        ItemNavegacion("Ajustes", Rutas.AJUSTES, Icons.Default.Settings)
    )

    val menuItems = when (rolActivo) {
        Rol.CLIENTE -> itemsCliente
        Rol.ADMIN -> itemsSoporte
        else -> emptyList()
    }

    val mostrarBottomBar = menuItems.any { it.ruta == currentRoute }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            if (mostrarBottomBar) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ) {
                    menuItems.forEach { item ->
                        val selected = (currentRoute == item.ruta)
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                if (currentRoute != item.ruta) {
                                    navController.navigate(item.ruta) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = item.icono,
                                    contentDescription = item.titulo
                                )
                            },
                            label = {
                                Text(text = item.titulo)
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Rutas.SPLASH,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Rutas.SPLASH) {
                SplashScreen(
                    preferenciasStore = preferenciasStore,
                    onNavigateNext = { rolGuardado ->
                        val destino = when (rolGuardado) {
                            Rol.CLIENTE -> Rutas.MIS_TICKETS
                            Rol.ADMIN -> Rutas.PANEL
                            null -> Rutas.LOGIN
                        }
                        navController.navigate(destino) {
                            popUpTo(Rutas.SPLASH) { inclusive = true }
                        }
                    }
                )
            }

            composable(Rutas.LOGIN) {
                LoginScreen(
                    viewModel = sesionViewModel,
                    onLoginSuccess = { rol ->
                        val destination = if (rol == Rol.ADMIN) Rutas.PANEL else Rutas.MIS_TICKETS
                        navController.navigate(destination) {
                            popUpTo(Rutas.LOGIN) { inclusive = true }
                        }
                    },
                    onIrARegistro = {
                        navController.navigate(Rutas.REGISTRO)
                    }
                )
            }

            composable(Rutas.REGISTRO) {
                val registroViewModel: com.galindo.ayudacampus.viewmodel.RegistroViewModel = viewModel(
                    factory = app.registroViewModelFactory
                )

                com.galindo.ayudacampus.ui.cliente.RegistroScreen(
                    viewModel = registroViewModel,
                    onRegistroExitoso = {
                        navController.popBackStack()
                    },
                    onVolver = {
                        navController.popBackStack()
                    }
                )
            }

            composable(Rutas.MIS_TICKETS) {
                val clienteId = usuarioActual?.id ?: 1
                val misTicketsViewModel: MisTicketsViewModel = viewModel(
                    factory = app.misTicketsViewModelFactory(clienteId)
                )

                MisTicketsScreen(
                    viewModel = misTicketsViewModel,
                    recibirTickets = recibirTickets,
                    onNuevoTicketClick = {
                        navController.navigate(Rutas.NUEVO_TICKET)
                    },
                    onTicketClick = { ticketId ->
                        navController.navigate("${Rutas.DETALLE_TICKET}/$ticketId")
                    }
                )
            }

            composable(Rutas.NUEVO_TICKET) {
                val clienteId = usuarioActual?.id ?: 1
                val nuevoTicketViewModel: NuevoTicketViewModel = viewModel(
                    factory = app.nuevoTicketViewModelFactory(clienteId)
                )

                NuevoTicketScreen(
                    viewModel = nuevoTicketViewModel,
                    recibirTickets = recibirTickets,
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable(Rutas.PERFIL) {
                PerfilScreen(
                    viewModel = sesionViewModel,
                    onCambiarPerfil = { nuevoRol ->
                        val destino = if (nuevoRol == Rol.ADMIN) Rutas.PANEL else Rutas.MIS_TICKETS
                        navController.navigate(destino) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    onCerrarSesion = {
                        navController.navigate(Rutas.LOGIN) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

            composable(Rutas.PANEL) {
                val panelViewModel: PanelViewModel = viewModel(factory = app.panelViewModelFactory)
                val bandejaViewModel: BandejaViewModel = viewModel(factory = app.bandejaViewModelFactory)

                PanelScreen(
                    viewModel = panelViewModel,
                    onVerAltasUrgentes = {
                        bandejaViewModel.onFiltroCambiado(com.galindo.ayudacampus.viewmodel.FiltroEstadoBandeja.ALTAS_URGENTES)
                        navController.navigate(Rutas.BANDEJA) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }

            composable(Rutas.BANDEJA) {
                val bandejaViewModel: BandejaViewModel = viewModel(factory = app.bandejaViewModelFactory)

                BandejaScreen(
                    viewModel = bandejaViewModel,
                    onTicketClick = { ticketId ->
                        navController.navigate("${Rutas.ATENDER_TICKET}/$ticketId")
                    }
                )
            }

            composable(Rutas.CATEGORIAS) {
                val categoriasViewModel: CategoriasViewModel = viewModel(factory = app.categoriasViewModelFactory)

                FormularioCategoriaScreen(
                    viewModel = categoriasViewModel
                )
            }

            composable(Rutas.AJUSTES) {
                AjustesScreen(
                    viewModel = sesionViewModel,
                    onCambiarPerfil = { nuevoRol ->
                        val destino = if (nuevoRol == Rol.ADMIN) Rutas.PANEL else Rutas.MIS_TICKETS
                        navController.navigate(destino) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    onCerrarSesion = {
                        navController.navigate(Rutas.LOGIN) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

            composable(
                route = "${Rutas.DETALLE_TICKET}/{ticketId}",
                arguments = listOf(
                    navArgument("ticketId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val ticketId = backStackEntry.arguments?.getInt("ticketId") ?: 0
                val clienteId = usuarioActual?.id ?: 1

                val ticketDetalleViewModel: TicketDetalleViewModel = viewModel(
                    factory = app.ticketDetalleViewModelFactory(ticketId, clienteId)
                )

                DetalleTicketScreen(
                    viewModel = ticketDetalleViewModel,
                    clienteId = clienteId,
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable(
                route = "${Rutas.ATENDER_TICKET}/{ticketId}",
                arguments = listOf(
                    navArgument("ticketId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val ticketId = backStackEntry.arguments?.getInt("ticketId") ?: 0
                val soporteId = usuarioActual?.id ?: 2

                val atenderViewModel: AtenderViewModel = viewModel(
                    factory = app.atenderViewModelFactory(ticketId, soporteId)
                )

                AtenderTicketScreen(
                    viewModel = atenderViewModel,
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
