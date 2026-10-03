package com.tecsup.mibodega.ui.cliente.screens.inicio

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.cliente.modelo.Producto
import com.tecsup.mibodega.ui.cliente.modelo.listaCategorias
import com.tecsup.mibodega.ui.componentes.BarraNavegacionInferior
import com.tecsup.mibodega.ui.componentes.ProductoCard
import com.tecsup.mibodega.ui.theme.GrisClaro
import com.tecsup.mibodega.ui.theme.VerdeBodega

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioScreen(
    productos: List<Producto>,
    favoritosIds: List<Int>,
    cantidadCarrito: Int,
    categoriaInicial: String = "Todos",
    onVerCarrito: () -> Unit,
    onToggleFavorito: (Producto) -> Unit,
    onAgregarCarrito: (Producto) -> Unit,
    onProductoClick: (Producto) -> Unit,
    onNavegar: (String) -> Unit
) {
    var categoriaSeleccionada by remember { mutableStateOf(categoriaInicial) }
    var textoBusqueda by remember { mutableStateOf("") }
    var ordenarMenorAMayor by remember { mutableStateOf(true) }

    val filtrados = productos.filter { producto ->
        val coincideCategoria = categoriaSeleccionada == "Todos" || producto.categoria == categoriaSeleccionada
        val coincideBusqueda = producto.nombre.contains(textoBusqueda, ignoreCase = true)
        coincideCategoria && coincideBusqueda
    }

    val ordenados = if (ordenarMenorAMayor) {
        filtrados.sortedBy { it.precio }
    } else {
        filtrados.sortedByDescending { it.precio }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Bodega") },
                actions = {
                    IconButton(onClick = onVerCarrito) {
                        BadgedBox(
                            badge = {
                                if (cantidadCarrito > 0) {
                                    Badge(containerColor = Color.Red, contentColor = Color.White) {
                                        Text("$cantidadCarrito")
                                    }
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
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = textoBusqueda,
                onValueChange = { textoBusqueda = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Buscar productos...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = GrisClaro,
                    focusedContainerColor = GrisClaro,
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = VerdeBodega
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(listaCategorias) { cat ->
                    val seleccionado = cat == categoriaSeleccionada
                    FilterChip(
                        selected = seleccionado,
                        onClick = { categoriaSeleccionada = cat },
                        label = { Text(cat) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = VerdeBodega,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = ordenarMenorAMayor,
                    onClick = { ordenarMenorAMayor = true },
                    label = { Text("Menor precio") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = VerdeBodega,
                        selectedLabelColor = Color.White
                    )
                )
                FilterChip(
                    selected = !ordenarMenorAMayor,
                    onClick = { ordenarMenorAMayor = false },
                    label = { Text("Mayor precio") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = VerdeBodega,
                        selectedLabelColor = Color.White
                    )
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(ordenados) { producto ->
                    val esFav = favoritosIds.contains(producto.id)
                    ProductoCard(
                        producto = producto,
                        esFavorito = esFav,
                        onFavoritoClick = { onToggleFavorito(producto) },
                        onAgregarClick = { onAgregarCarrito(producto) },
                        onClick = { onProductoClick(producto) }
                    )
                }
            }
        }
    }
}
