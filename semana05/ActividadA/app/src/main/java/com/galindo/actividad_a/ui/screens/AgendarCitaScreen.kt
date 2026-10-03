package com.galindo.actividad_a.navigation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.galindo.actividad_a.data.listaFechas
import com.galindo.actividad_a.data.listaHoras
import com.galindo.actividad_a.model.Cita
import com.galindo.actividad_a.model.Medico
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendarCitaScreen(
    medico: Medico,
    navController: NavController,
    onCitaConfirmada: (Cita) -> Unit
) {
    // Guardan la fecha y hora elegidas. Empiezan en null porque nada está seleccionado.
    // Esto funciona como un RadioButton: solo una opción puede estar activa a la vez.
    var fechaSeleccionada by remember { mutableStateOf<String?>(null) }
    var horaSeleccionada by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Agendar cita con ${medico.nombre}", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(16.dp))
        Text("Elige una fecha")
        LazyRow {
            items(listaFechas) { fecha ->
                FilterChip(
                    selected = fecha == fechaSeleccionada,
                    onClick = { fechaSeleccionada = fecha },
                    label = { Text(fecha) },
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Elige una hora")
        LazyRow {
            items(listaHoras) { hora ->
                FilterChip(
                    selected = hora == horaSeleccionada,
                    onClick = { horaSeleccionada = hora },
                    label = { Text(hora) },
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            // El botón solo se activa si ya eligió fecha Y hora
            enabled = fechaSeleccionada != null && horaSeleccionada != null,
            onClick = {
                val fecha = fechaSeleccionada!!
                val hora = horaSeleccionada!!
                // Guarda la cita en la lista compartida de toda la app
                onCitaConfirmada(Cita(medico = medico, fecha = fecha, hora = hora))
                // Navega a la pantalla de confirmación pasando los datos por la ruta
                navController.navigate("confirmacion/${medico.nombre}/${fecha}/${hora}")
            }
        ) {
            Text("Confirmar cita")
        }
    }
}