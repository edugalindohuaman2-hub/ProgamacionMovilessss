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
import com.galindo.actividad_a.model.Medico

@Composable
fun PerfilMedicoScreen(medico: Medico, navController: NavController) {
    // Esta pantalla recibe el médico ya elegido (viene de InicioScreen por el id)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(medico.nombre, style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(4.dp))
        Text(medico.especialidad, style = MaterialTheme.typography.bodyLarge)
        Text("Calificación: ⭐ ${medico.calificacion}")

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            // Navega a la pantalla de agendar, pasando el id del médico otra vez
            navController.navigate("agendar/${medico.id}")
        }) {
            Text("Agendar cita")
        }
    }
}