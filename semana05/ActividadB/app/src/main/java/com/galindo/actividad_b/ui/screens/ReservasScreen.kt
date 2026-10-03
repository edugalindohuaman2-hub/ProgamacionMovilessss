package com.tecsupfit.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tecsupfit.app.model.Reserva

@Composable
fun ReservasScreen(reservas: List<Reserva>) {
    if (reservas.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Aún no tienes reservas")
        }
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(reservas) { reserva ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(reserva.clase.nombre, style = MaterialTheme.typography.titleMedium)
                        Text(reserva.horarioSeleccionado, style = MaterialTheme.typography.bodyMedium)
                    }
                    AssistChip(
                        onClick = { /* solo informativo */ },
                        label = { Text(reserva.estado) },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = if (reserva.estado == "Confirmada")
                                Color(0xFFDFF5E1) else Color(0xFFE0E0E0)
                        )
                    )
                }
            }
        }
    }
}
