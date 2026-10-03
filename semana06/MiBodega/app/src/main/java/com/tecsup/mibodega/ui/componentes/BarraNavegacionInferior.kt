package com.tecsup.mibodega.ui.componentes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@Composable
fun BarraNavegacionInferior(
    rutaActual: String,
    onNavegar: (String) -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
            label = { Text("Inicio") },
            selected = rutaActual == "inicio",
            onClick = { onNavegar("inicio") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Search, contentDescription = "Categorías") },
            label = { Text("Categorías") },
            selected = rutaActual == "categorias",
            onClick = { onNavegar("categorias") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Favorite, contentDescription = "Favoritos") },
            label = { Text("Favoritos") },
            selected = rutaActual == "favoritos",
            onClick = { onNavegar("favoritos") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.List, contentDescription = "Pedidos") },
            label = { Text("Pedidos") },
            selected = rutaActual == "pedidos",
            onClick = { onNavegar("pedidos") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
            label = { Text("Perfil") },
            selected = rutaActual == "perfil",
            onClick = { onNavegar("perfil") }
        )
    }
}
