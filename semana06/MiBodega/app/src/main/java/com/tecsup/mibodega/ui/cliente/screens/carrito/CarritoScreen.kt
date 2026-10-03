package com.tecsup.mibodega.ui.cliente.screens.carrito

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.tecsup.mibodega.ui.cliente.modelo.ItemCarrito
import com.tecsup.mibodega.ui.cliente.modelo.Producto
import com.tecsup.mibodega.ui.theme.GrisBorde
import com.tecsup.mibodega.ui.theme.GrisClaro
import com.tecsup.mibodega.ui.theme.RojoPrecio
import com.tecsup.mibodega.ui.theme.VerdeBodega

private const val COSTO_DELIVERY = 4.00

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(
    carrito: List<ItemCarrito>,
    onVolver: () -> Unit,
    onIncrementar: (Producto) -> Unit,
    onDecrementar: (Producto) -> Unit,
    onEliminar: (Producto) -> Unit,
    onVaciarCarrito: () -> Unit,
    onContinuarPedido: () -> Unit
) {
    var productoAEliminar by remember { mutableStateOf<Producto?>(null) }
    var vaciarConfirmacion by remember { mutableStateOf(false) }

    if (productoAEliminar != null) {
        AlertDialog(
            onDismissRequest = { productoAEliminar = null },
            title = { Text("Eliminar producto") },
            text = { Text("¿Deseas eliminar este producto del carrito?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onEliminar(productoAEliminar!!)
                        productoAEliminar = null
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { productoAEliminar = null }) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (vaciarConfirmacion) {
        AlertDialog(
            onDismissRequest = { vaciarConfirmacion = false },
            title = { Text("Vaciar carrito") },
            text = { Text("¿Deseas eliminar todos los productos del carrito?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onVaciarCarrito()
                        vaciarConfirmacion = false
                    }
                ) {
                    Text("Vaciar")
                }
            },
            dismissButton = {
                TextButton(onClick = { vaciarConfirmacion = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    val subtotal = carrito.sumOf { it.producto.precio * it.cantidad }
    val total = if (carrito.isNotEmpty()) subtotal + COSTO_DELIVERY else 0.0

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi carrito") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    if (carrito.isNotEmpty()) {
                        IconButton(onClick = { vaciarConfirmacion = true }) {
                            Icon(Icons.Default.Delete, contentDescription = "Vaciar carrito")
                        }
                    }
                }
            )
        },
        bottomBar = {
            if (carrito.isNotEmpty()) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    tonalElevation = 8.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Subtotal", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(text = "S/ %.2f".format(subtotal))
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Costo de delivery", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(text = "S/ %.2f".format(COSTO_DELIVERY))
                        }
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Total", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text(text = "S/ %.2f".format(total), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = VerdeBodega)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = onContinuarPedido,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp),
                            shape = RoundedCornerShape(27.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = VerdeBodega)
                        ) {
                            Text("Continuar pedido", style = MaterialTheme.typography.titleMedium, color = Color.White)
                        }
                    }
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            if (carrito.isEmpty()) {
                Text(
                    text = "Tu carrito está vacío",
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.titleMedium
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(carrito) { item ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, GrisBorde),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(GrisClaro),
                                    contentAlignment = Alignment.Center
                                ) {
                                    AsyncImage(
                                        model = item.producto.imageUrl,
                                        contentDescription = item.producto.nombre,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = item.producto.nombre,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "S/ %.2f".format(item.producto.precio),
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = RojoPrecio
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(
                                        onClick = { onDecrementar(item.producto) },
                                        modifier = Modifier
                                            .size(32.dp)
                                            .background(GrisClaro, CircleShape)
                                    ) {
                                        Text("-", style = MaterialTheme.typography.titleMedium)
                                    }
                                    Text(
                                        text = "${item.cantidad}",
                                        modifier = Modifier.padding(horizontal = 8.dp),
                                        fontWeight = FontWeight.Bold
                                    )
                                    IconButton(
                                        onClick = { onIncrementar(item.producto) },
                                        modifier = Modifier
                                            .size(32.dp)
                                            .background(GrisClaro, CircleShape)
                                    ) {
                                        Text("+", style = MaterialTheme.typography.titleMedium)
                                    }
                                    IconButton(onClick = { productoAEliminar = item.producto }) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Eliminar",
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
