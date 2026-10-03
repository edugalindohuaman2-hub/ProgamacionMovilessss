package com.tecsupfit.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PerfilScreen(totalReservas: Int) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(16.dp))
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = null,
            modifier = Modifier.size(96.dp)
        )
        Spacer(Modifier.height(12.dp))
        Text("Edu Galindo ", style = MaterialTheme.typography.headlineSmall)
        Text("edu.galindo@tecsup.edu.pe", style = MaterialTheme.typography.bodyMedium)

        Spacer(Modifier.height(24.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
            EstadisticaItem(valor = "$totalReservas", etiqueta = "Clases reservadas")
            EstadisticaItem(valor = "12", etiqueta = "Racha (días)")
        }
    }
}

@Composable
private fun EstadisticaItem(valor: String, etiqueta: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(valor, style = MaterialTheme.typography.headlineMedium)
        Text(etiqueta, style = MaterialTheme.typography.bodySmall)
    }
}
