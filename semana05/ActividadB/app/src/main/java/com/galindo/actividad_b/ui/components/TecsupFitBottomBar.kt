package com.tecsupfit.app.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.tecsupfit.app.navigation.Screen

private data class BottomItem(val route: String, val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)

private val items = listOf(
    BottomItem(Screen.Inicio.route, "Inicio", Icons.Default.Home),
    BottomItem(Screen.Reservas.route, "Reservas", Icons.Default.DateRange),
    BottomItem(Screen.Rutinas.route, "Rutinas", Icons.Default.List),
    BottomItem(Screen.Perfil.route, "Perfil", Icons.Default.Person)
)

@Composable
fun TecsupFitBottomBar(navController: NavController, currentRoute: String?) {
    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(Screen.Inicio.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) }
            )
        }
    }
}
