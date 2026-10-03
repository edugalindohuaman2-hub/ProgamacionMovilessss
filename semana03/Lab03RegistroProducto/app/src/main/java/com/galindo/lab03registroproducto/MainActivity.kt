package com.galindo.lab03registroproducto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.galindo.lab03registroproducto.ui.theme.Lab03RegistroProductoTheme
import java.util.Locale
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab03RegistroProductoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()){ innerPadding ->

                        PantallaRegistro(
                            modifier = Modifier.padding(innerPadding)
                        )
                }
            }
        }
    }
}

@Composable
fun PantallaRegistro(modifier: Modifier = Modifier) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        var nombre by remember {
            mutableStateOf("")
        }

        var precio by remember {
            mutableStateOf("")
        }

        var cantidad by remember {
            mutableStateOf("")
        }

        var mostrarResumen by remember {
            mutableStateOf(false)
        }
        var errorMensaje by remember {
            mutableStateOf("") }


        Text(
            text = "Nuevo producto",
            style = MaterialTheme.typography.headlineSmall
        )

        Text(
            text = "Completa los datos y presiona Agregar",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )
        OutlinedTextField(
            value = nombre,
            onValueChange = {
                nombre = it
            },
            label = {
                Text("Nombre del producto")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {

            OutlinedTextField(
                value = precio,
                onValueChange = {
                    precio = it
                },
                label = {
                    Text("Precio (S/)")
                },
                modifier = Modifier.weight(1f)
            )

            Spacer(
                modifier = Modifier.width(16.dp)
            )

            OutlinedTextField(
                value = cantidad,
                onValueChange = {
                    cantidad = it
                },
                label = {
                    Text("Cantidad")
                },
                modifier = Modifier.weight(1f)
            )
        }
            Spacer(
                modifier = Modifier.height(24.dp)
            )

            Row(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = {
                        // Validar si hay campos vacíos
                        if (nombre.isBlank() || precio.isBlank() || cantidad.isBlank()) {
                            mostrarResumen = false
                            errorMensaje = "Por favor, complete todos los campos."
                        } else if (precio.toDoubleOrNull() == null || cantidad.toIntOrNull() == null) {
                            mostrarResumen = false
                            errorMensaje = "Ingrese un precio y cantidad válidos."
                        } else {
                            errorMensaje = ""
                            mostrarResumen = true
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("AGREGAR PRODUCTO")
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        nombre = ""
                        precio = ""
                        cantidad = ""
                        mostrarResumen = false
                        errorMensaje = ""
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("LIMPIAR")
                }
            }

        // Mensaje de error (solo visible si no está vacío)
        if (errorMensaje.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = errorMensaje,
                color = Color.Red,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Card de Resumen
        if (mostrarResumen) {

            val precioNum = precio.toDoubleOrNull() ?: 0.0
            val cantidadNum = cantidad.toIntOrNull() ?: 0
            val importe = precioNum * cantidadNum

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Text(
                        text = nombre,
                        style = MaterialTheme.typography.titleLarge
                    )

                    Text(
                        text = "Precio: S/ " + String.format(Locale.US, "%.2f", precioNum)
                    )

                    Text(
                        text = "Cantidad: $cantidadNum"
                    )

                    Text(
                        text = "Importe: S/ " + String.format(Locale.US, "%.2f", importe),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "✓ Producto registrado correctamente",
                        color = Color(0xFF2E7D32)
                    )
                    }
                }
            }
        }
    }


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

