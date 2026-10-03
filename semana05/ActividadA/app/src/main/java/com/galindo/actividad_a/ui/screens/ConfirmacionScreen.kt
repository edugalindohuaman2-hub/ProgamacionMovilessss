package com.galindo.actividad_a.navigation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun ConfirmacionScreen(
    medicoNombre: String,
    fecha: String,
    hora: String,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("¡Cita agendada!", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Médico: $medicoNombre")
        Text("Fecha: $fecha")
        Text("Hora: $hora")

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            // Vuelve a Inicio y limpia el historial de pantallas anteriores
            navController.navigate("inicio") {
                popUpTo("inicio") { inclusive = true }
            }
        }) {
            Text("Volver al inicio")
        }
    }
}