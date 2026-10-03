package com.galindo.actividad_a.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.galindo.actividad_a.data.listaMedicos
import com.galindo.actividad_a.model.Cita
import com.galindo.actividad_a.ui.components.MenuDrawerFijo
import com.galindo.actividad_a.ui.screens.AgendarCitaScreen
import com.galindo.actividad_a.ui.screens.ConfirmacionScreen
import com.galindo.actividad_a.ui.screens.HistorialScreen
import com.galindo.actividad_a.ui.screens.InicioScreen
import com.galindo.actividad_a.ui.screens.MisCitasScreen
import com.galindo.actividad_a.ui.screens.PerfilMedicoScreen
import com.galindo.actividad_a.ui.screens.PerfilUsuarioScreen

@Composable
fun AppRoot() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Estado para controlar si el menú lateral (drawer) está abierto u oculto
    var isDrawerOpen by remember { mutableStateOf(true) }

    // Lista de citas inicial con fechas y horas legibles
    val listaCitas = remember {
        mutableStateListOf(
            Cita(medico = listaMedicos[0], fecha = "Lunes, 17 de Octubre", hora = "6:44 PM", estado = "Confirmada"),
            Cita(medico = listaMedicos[1], fecha = "Viernes, 21 de Octubre", hora = "11:00 AM", estado = "Completada"),
            Cita(medico = listaMedicos[2], fecha = "Miércoles, 26 de Octubre", hora = "3:30 PM", estado = "Confirmada")
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxSize()) {
            // Menú lateral (drawer) animado con transición de 300ms hacia la izquierda
            AnimatedVisibility(
                visible = isDrawerOpen,
                enter = slideInHorizontally(animationSpec = tween(300)) { -it } + fadeIn(animationSpec = tween(300)),
                exit = slideOutHorizontally(animationSpec = tween(300)) { -it } + fadeOut(animationSpec = tween(300))
            ) {
                Row {
                    MenuDrawerFijo(
                        currentRoute = currentRoute ?: "misCitas",
                        onRouteSelected = { route ->
                            if (currentRoute != route) {
                                navController.navigate(route) {
                                    popUpTo("misCitas") { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        onCollapse = { isDrawerOpen = false },
                    )

                    VerticalDivider(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        thickness = 1.dp
                    )
                }
            }

            // Pantalla de contenido principal que se expande para ocupar todo el espacio disponible
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.TopCenter
            ) {
                NavHost(
                    navController = navController,
                    startDestination = "misCitas",
                    enterTransition = { fadeIn(animationSpec = tween(300)) },
                    exitTransition = { fadeOut(animationSpec = tween(300)) },
                    popEnterTransition = { fadeIn(animationSpec = tween(300)) },
                    popExitTransition = { fadeOut(animationSpec = tween(300)) }
                ) {
                    composable("inicio") {
                        InicioScreen(navController = navController)
                    }

                    composable(
                        route = "perfil/{medicoId}",
                        arguments = listOf(navArgument("medicoId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val medicoId = backStackEntry.arguments?.getString("medicoId")
                        val medico = listaMedicos.find { it.id == medicoId }
                        medico?.let {
                            PerfilMedicoScreen(medico = it, navController = navController)
                        }
                    }

                    composable(
                        route = "agendar/{medicoId}",
                        arguments = listOf(navArgument("medicoId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val medicoId = backStackEntry.arguments?.getString("medicoId")
                        val medico = listaMedicos.find { it.id == medicoId }
                        medico?.let {
                            AgendarCitaScreen(
                                medico = it,
                                navController = navController,
                                onCitaConfirmada = { nuevaCita -> listaCitas.add(0, nuevaCita) }
                            )
                        }
                    }

                    composable(
                        route = "confirmacion/{medicoNombre}/{fecha}/{hora}",
                        arguments = listOf(
                            navArgument("medicoNombre") { type = NavType.StringType },
                            navArgument("fecha") { type = NavType.StringType },
                            navArgument("hora") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val nombre = backStackEntry.arguments?.getString("medicoNombre") ?: ""
                        val fecha = backStackEntry.arguments?.getString("fecha") ?: ""
                        val hora = backStackEntry.arguments?.getString("hora") ?: ""
                        ConfirmacionScreen(
                            medicoNombre = nombre,
                            fecha = fecha,
                            hora = hora,
                            navController = navController
                        )
                    }

                    composable("misCitas") {
                        MisCitasScreen(listaCitas = listaCitas)
                    }

                    composable("historial") {
                        HistorialScreen()
                    }

                    composable("perfilUsuario") {
                        PerfilUsuarioScreen()
                    }
                }
            }
        }

        // Botón flotante con ícono "→" que aparece cuando el menú está oculto (isDrawerOpen == false)
        AnimatedVisibility(
            visible = !isDrawerOpen,
            enter = fadeIn(animationSpec = tween(300)),
            exit = fadeOut(animationSpec = tween(300)),
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            Surface(
                onClick = { isDrawerOpen = true },
                shape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp),
                color = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shadowElevation = 6.dp,
                modifier = Modifier
                    .padding(top = 20.dp)
                    .size(width = 44.dp, height = 48.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Mostrar menú",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}
