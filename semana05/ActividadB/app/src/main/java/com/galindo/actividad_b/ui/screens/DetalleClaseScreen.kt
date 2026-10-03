package com.tecsupfit.app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsupfit.app.model.Clase

@Composable
fun DetalleClaseScreen(
    clase: Clase,
    onReservar: (horarioElegido: String) -> Unit
) {
    // Selección única: se comporta como RadioButton aunque el requisito
    // original hablaba de "chips" -> aquí se usa RadioButton explícito
    // para dejar clara la semántica de opción única.
    var horarioSeleccionado by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(clase.nombre, style = MaterialTheme.typography.headlineSmall)
        Text("Instructor: ${clase.instructor}", style = MaterialTheme.typography.bodyMedium)

        Spacer(Modifier.height(20.dp))
        Text("Elige un horario:", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        Column {
            clase.horarios.forEach { horario ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { horarioSeleccionado = horario }
                        .padding(vertical = 8.dp)
                ) {
                    RadioButton(
                        selected = horarioSeleccionado == horario,
                        onClick = { horarioSeleccionado = horario }
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(horario)
                }
            }
        }

        Spacer(Modifier.weight(1f))

        Button(
            onClick = { horarioSeleccionado?.let(onReservar) },
            enabled = horarioSeleccionado != null,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Reservar cupo")
        }
    }
}
