package com.tecsup.mibodega.ui.cliente.screens.favoritos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.cliente.modelo.Producto
import com.tecsup.mibodega.ui.componentes.BarraNavegacionInferior
import com.tecsup.mibodega.ui.componentes.ProductoCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritosScreen(
    productosFavoritos: List<Producto>,
    onToggleFavorito: (Producto) -> Unit,
    onAgregarCarrito: (Producto) -> Unit,
    onProductoClick: (Producto) -> Unit,
    onNavegar: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Favoritos") }
            )
        },
        bottomBar = {
            BarraNavegacionInferior(
                rutaActual = "favoritos",
                onNavegar = onNavegar
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            if (productosFavoritos.isEmpty()) {
                Text(
                    text = "No tienes productos favoritos guardados.",
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(productosFavoritos) { producto ->
                        ProductoCard(
                            producto = producto,
                            esFavorito = true,
                            onFavoritoClick = { onToggleFavorito(producto) },
                            onAgregarClick = { onAgregarCarrito(producto) },
                            onClick = { onProductoClick(producto) }
                        )
                    }
                }
            }
        }
    }
}
