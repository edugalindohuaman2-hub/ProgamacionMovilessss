package com.tecsup.mibodega.ui.cliente.screens.entrega

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatosEntregaScreen(
    subtotal: Double,
    onVolver: () -> Unit,
    onConfirmarPedido: (tipoEntrega: String, direccion: String, totalFinal: Double) -> Unit
) {
    var direccion by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var tipoEntrega by remember { mutableStateOf("Delivery") }
    var intentado by remember { mutableStateOf(false) }

    val errorDireccion = intentado && tipoEntrega == "Delivery" && direccion.isBlank()
    val errorTelefono = intentado && telefono.isBlank()

    val costoEnvio = if (tipoEntrega == "Delivery") 5.00 else 0.00
    val totalFinal = subtotal + costoEnvio

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Datos de Entrega") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(text = "Seleccione el tipo de entrega:", style = MaterialTheme.typography.titleMedium)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                RadioButton(
                    selected = tipoEntrega == "Delivery",
                    onClick = { tipoEntrega = "Delivery" }
                )
                Text(text = "Delivery (+S/ 5.00)")
                Spacer(modifier = Modifier.width(16.dp))
                RadioButton(
                    selected = tipoEntrega == "Recojo en tienda",
                    onClick = { tipoEntrega = "Recojo en tienda" }
                )
                Text(text = "Recojo en tienda (Gratis)")
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (tipoEntrega == "Delivery") {
                OutlinedTextField(
                    value = direccion,
                    onValueChange = { direccion = it },
                    label = { Text("Dirección de entrega") },
                    isError = errorDireccion,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                if (errorDireccion) {
                    Text(text = "Campo obligatorio para delivery", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            OutlinedTextField(
                value = telefono,
                onValueChange = { telefono = it },
                label = { Text("Teléfono de contacto") },
                isError = errorTelefono,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            if (errorTelefono) {
                Text(text = "Campo obligatorio", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(text = "Subtotal: S/ %.2f".format(subtotal))
            Text(text = "Envio: S/ %.2f".format(costoEnvio))
            Text(text = "Total a pagar: S/ %.2f".format(totalFinal), style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    intentado = true
                    val dirValida = if (tipoEntrega == "Delivery") direccion.isNotBlank() else true
                    if (dirValida && telefono.isNotBlank()) {
                        onConfirmarPedido(tipoEntrega, if (tipoEntrega == "Delivery") direccion else "Tienda principal", totalFinal)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Confirmar Pedido")
            }
        }
    }
}
