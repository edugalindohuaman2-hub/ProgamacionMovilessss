package com.tecsupfit.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsupfit.app.model.Reserva

@Composable
fun ConfirmacionScreen(
    reserva: Reserva?,
    onVerReservas: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            modifier = Modifier.size(64.dp)
        )
        Spacer(Modifier.height(16.dp))
        Text("¡Reserva confirmada!", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))

        if (reserva != null) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Clase: ${reserva.clase.nombre}")
                    Text("Horario: ${reserva.horarioSeleccionado}")
                    Text("Instructor: ${reserva.clase.instructor}")
                }
            }
        }

        Spacer(Modifier.height(24.dp))
        Button(onClick = onVerReservas, modifier = Modifier.fillMaxWidth()) {
            Text("Ver mis reservas")
        }
    }
}
