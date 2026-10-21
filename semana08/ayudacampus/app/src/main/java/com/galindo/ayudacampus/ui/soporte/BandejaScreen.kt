package com.galindo.ayudacampus.ui.soporte

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.galindo.ayudacampus.ui.components.TicketCard
import com.galindo.ayudacampus.viewmodel.BandejaViewModel
import com.galindo.ayudacampus.viewmodel.FiltroEstadoBandeja

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BandejaScreen(
    viewModel: BandejaViewModel,
    modifier: Modifier = Modifier,
    onTicketClick: (Int) -> Unit = {}
) {
    val tickets by viewModel.tickets.collectAsState()
    val filtroEstado by viewModel.filtroEstado.collectAsState()
    val busqueda by viewModel.busqueda.collectAsState()
    val filtroPrioridad by viewModel.filtroPrioridad.collectAsState()
    val filtroCategoria by viewModel.filtroCategoria.collectAsState()
    val categorias by viewModel.categorias.collectAsState()

    val opcionesPrioridad = listOf("Todas", "ALTA", "MEDIA", "BAJA")

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Bandeja de Incidencias TI",
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // 1. Campo de Búsqueda por Texto Libre
            OutlinedTextField(
                value = busqueda,
                onValueChange = { viewModel.onBusquedaCambiada(it) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Buscar por título, descripción o cliente...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar"
                    )
                },
                trailingIcon = {
                    if (busqueda.isNotEmpty()) {
                        IconButton(onClick = { viewModel.onBusquedaCambiada("") }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Limpiar"
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 2. Chips de Estado
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FiltroEstadoBandeja.entries.forEach { filtro ->
                    FilterChip(
                        selected = (filtroEstado == filtro),
                        onClick = { viewModel.onFiltroCambiado(filtro) },
                        label = { Text(filtro.etiqueta) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // 3. Chips de Prioridad y Categoría
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Selector de Prioridad
                Text(
                    text = "Prioridad:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                opcionesPrioridad.forEach { prio ->
                    FilterChip(
                        selected = (filtroPrioridad.equals(prio, ignoreCase = true)),
                        onClick = { viewModel.onPrioridadSeleccionada(prio) },
                        label = { Text(prio) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.secondary,
                            selectedLabelColor = MaterialTheme.colorScheme.onSecondary
                        )
                    )
                }

                // Selector de Categorías
                if (categorias.isNotEmpty()) {
                    Text(
                        text = "Categoría:",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    FilterChip(
                        selected = (filtroCategoria.equals("Todas", ignoreCase = true)),
                        onClick = { viewModel.onCategoriaSeleccionada("Todas") },
                        label = { Text("Todas") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.tertiary,
                            selectedLabelColor = MaterialTheme.colorScheme.onTertiary
                        )
                    )

                    categorias.forEach { cat ->
                        FilterChip(
                            selected = (filtroCategoria.equals(cat.nombre, ignoreCase = true)),
                            onClick = { viewModel.onCategoriaSeleccionada(cat.nombre) },
                            label = { Text(cat.nombre) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.tertiary,
                                selectedLabelColor = MaterialTheme.colorScheme.onTertiary
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Lista de Tickets o Estado Vacío
            if (tickets.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Inbox,
                            contentDescription = "Bandeja vacía",
                            tint = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No hay incidencias en esta vista",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "No se encontraron tickets registrados con el filtro seleccionado.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(
                        items = tickets,
                        key = { it.ticket.id }
                    ) { item ->
                        TicketCard(
                            item = item,
                            mostrarCliente = true,
                            onClick = { onTicketClick(item.ticket.id) }
                        )
                    }
                }
            }
        }
    }
}
