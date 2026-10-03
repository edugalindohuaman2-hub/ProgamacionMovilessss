package com.galindo.actividad_a.navigation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.galindo.actividad_a.model.Cita
@Composable
fun MisCitasScreen(listaCitas: List<Cita>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Mis citas", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(12.dp))

        if (listaCitas.isEmpty()) {
            Text("Todavía no tienes citas agendadas.")
        } else {
            LazyColumn {
                items(listaCitas) { cita ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(cita.medico.nombre, style = MaterialTheme.typography.titleSmall)
                                Text("${cita.fecha} - ${cita.hora}")
                            }
                            val color = if (cita.estado == "Confirmada") {
                                Color(0xFF2E7D32)
                            } else {
                                Color(0xFF757575)
                            }
                            Text(cita.estado, color = color)
                        }
                    }
                }
            }
        }
    }
}