package com.tecsup.mibodega.ui.cliente.screens.categorias

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.cliente.modelo.listaCategorias
import com.tecsup.mibodega.ui.componentes.BarraNavegacionInferior

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriasScreen(
    onCategoriaClick: (String) -> Unit,
    onNavegar: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Categorías") }
            )
        },
        bottomBar = {
            BarraNavegacionInferior(
                rutaActual = "categorias",
                onNavegar = onNavegar
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(listaCategorias) { categoria ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onCategoriaClick(categoria) },
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Text(
                        text = categoria,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}
