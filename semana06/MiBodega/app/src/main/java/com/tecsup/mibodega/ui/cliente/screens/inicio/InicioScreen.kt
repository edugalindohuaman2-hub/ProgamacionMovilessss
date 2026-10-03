package com.tecsup.mibodega.ui.cliente.screens.inicio

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.cliente.modelo.Producto
import com.tecsup.mibodega.ui.cliente.modelo.listaCategorias
import com.tecsup.mibodega.ui.componentes.BarraNavegacionInferior
import com.tecsup.mibodega.ui.componentes.ProductoCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioScreen(
    productos: List<Producto>,
    favoritosIds: List<Int>,
    cantidadCarrito: Int,
    onVerCarrito: () -> Unit,
    onToggleFavorito: (Producto) -> Unit,
    onAgregarCarrito: (Producto) -> Unit,
    onNavegar: (String) -> Unit
) {
    var categoriaSeleccionada by remember { mutableStateOf("Todos") }
    var ordenarMenorAMayor by remember { mutableStateOf(true) }

    val filtrados = productos.filter {
        categoriaSeleccionada == "Todos" || it.categoria == categoriaSeleccionada
    }

    val ordenados = if (ordenarMenorAMayor) {
        filtrados.sortedBy { it.precio }
    } else {
        filtrados.sortedByDescending { it.precio }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Bodega - Inicio") },
                actions = {
                    IconButton(onClick = onVerCarrito) {
                        BadgedBox(
                            badge = {
                                if (cantidadCarrito > 0) {
                                    Badge { Text("$cantidadCarrito") }
                                }
                            }
                        ) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito")
                        }
                    }
                }
            )
        },
        bottomBar = {
            BarraNavegacionInferior(
                rutaActual = "inicio",
                onNavegar = onNavegar
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(listaCategorias) { cat ->
                    FilterChip(
                        selected = categoriaSeleccionada == cat,
                        onClick = { categoriaSeleccionada = cat },
                        label = { Text(cat) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = { ordenarMenorAMayor = true }) {
                    Text("Precio: Menor a Mayor")
                }
                Button(onClick = { ordenarMenorAMayor = false }) {
                    Text("Precio: Mayor a Menor")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(ordenados) { producto ->
                    val esFav = favoritosIds.contains(producto.id)
                    ProductoCard(
                        producto = producto,
                        esFavorito = esFav,
                        onFavoritoClick = { onToggleFavorito(producto) },
                        onAgregarClick = { onAgregarCarrito(producto) }
                    )
                }
            }
        }
    }
}
