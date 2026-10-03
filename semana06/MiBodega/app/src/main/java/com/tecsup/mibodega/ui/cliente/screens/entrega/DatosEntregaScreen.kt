package com.tecsup.mibodega.ui.cliente.screens.entrega

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.theme.VerdeBodega

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatosEntregaScreen(
    subtotal: Double,
    nombreInicial: String,
    telefonoInicial: String,
    direccionInicial: String,
    onVolver: () -> Unit,
    onConfirmarPedido: (tipoEntrega: String, direccion: String, metodoPago: String, totalFinal: Double) -> Unit
) {
    var nombre by remember { mutableStateOf(nombreInicial.ifBlank { "Juan Pérez" }) }
    var telefono by remember { mutableStateOf(telefonoInicial.ifBlank { "987 654 321" }) }
    var direccion by remember { mutableStateOf(direccionInicial.ifBlank { "Av. Los Olivos 123" }) }
    var referencia by remember { mutableStateOf("Frente al parque") }
    var metodoPago by remember { mutableStateOf("Efectivo al entregar") }
    var intentado by remember { mutableStateOf(false) }

    val errorDireccion = intentado && direccion.isBlank()
    val errorTelefono = intentado && telefono.isBlank()
    val errorNombre = intentado && nombre.isBlank()

    val costoEnvio = 4.00
    val totalFinal = subtotal + costoEnvio

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Datos de entrega") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        bottomBar = {
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
                        Text(text = "Total a pagar", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text(text = "S/ %.2f".format(totalFinal), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = VerdeBodega)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            intentado = true
                            if (nombre.isNotBlank() && telefono.isNotBlank() && direccion.isNotBlank()) {
                                onConfirmarPedido("Delivery", direccion, metodoPago, totalFinal)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(27.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = VerdeBodega)
                    ) {
                        Text("Confirmar pedido", style = MaterialTheme.typography.titleMedium, color = Color.White)
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(text = "Información de contacto", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre completo") },
                isError = errorNombre,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = telefono,
                onValueChange = { telefono = it },
                label = { Text("Teléfono") },
                isError = errorTelefono,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = direccion,
                onValueChange = { direccion = it },
                label = { Text("Dirección de entrega") },
                isError = errorDireccion,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = referencia,
                onValueChange = { referencia = it },
                label = { Text("Referencia") },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))
            Text(text = "Método de pago", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = metodoPago == "Efectivo al entregar",
                            onClick = { metodoPago = "Efectivo al entregar" }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(VerdeBodega, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.AttachMoney, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = "Efectivo al entregar", fontWeight = FontWeight.Medium)
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = metodoPago == "Yape",
                            onClick = { metodoPago = "Yape" }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(Color(0xFF7A288A), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "Y", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = "Yape", fontWeight = FontWeight.Medium)
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = metodoPago == "Plin",
                            onClick = { metodoPago = "Plin" }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(Color(0xFF00A3E0), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "P", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = "Plin", fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}
