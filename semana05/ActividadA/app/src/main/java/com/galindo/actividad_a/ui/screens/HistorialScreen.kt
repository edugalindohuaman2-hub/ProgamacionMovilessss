package com.galindo.actividad_a.navigation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HistorialScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Historial médico", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(12.dp))
        Text("Aquí puedes mostrar consultas pasadas, diagnósticos o notas.")
        Text("Tip: puedes ampliar esto con una LazyColumn de registros si quieres sumar más detalle.")
    }
}