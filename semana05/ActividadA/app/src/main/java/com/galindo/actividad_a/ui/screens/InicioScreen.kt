package com.galindo.actividad_a.navigation.screens


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.galindo.actividad_a.data.listaEspecialidades
import com.galindo.actividad_a.data.listaMedicos
import androidx.compose.foundation.lazy.items

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioScreen(navController: NavController) {
    // Guarda qué especialidad está seleccionada en el filtro (empieza en "Todas")
    var especialidadSeleccionada by remember { mutableStateOf("Todas") }

    // Filtra la lista de médicos según la especialidad elegida
    val medicosFiltrados = if (especialidadSeleccionada == "Todas") {
        listaMedicos
    } else {
        listaMedicos.filter { it.especialidad == especialidadSeleccionada }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        Text("Especialidades", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        // Fila horizontal de chips (LazyRow) con las especialidades
        LazyRow {
            items(listaEspecialidades) { especialidad ->
                FilterChip(
                    selected = especialidad == especialidadSeleccionada,
                    onClick = { especialidadSeleccionada = especialidad },
                    label = { Text(especialidad) },
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Médicos disponibles", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        // Lista vertical (LazyColumn) con una tarjeta por médico
        LazyColumn {
            items(medicosFiltrados) { medico ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    onClick = {
                        // Al tocar la tarjeta, navega al perfil pasando el id del médico
                        navController.navigate("perfil/${medico.id}")
                    }
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(medico.nombre, style = MaterialTheme.typography.titleSmall)
                        Text(medico.especialidad)
                        Text("⭐ ${medico.calificacion}")
                    }
                }
            }
        }
    }
}