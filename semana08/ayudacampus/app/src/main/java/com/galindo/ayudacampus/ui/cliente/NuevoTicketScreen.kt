package com.galindo.ayudacampus.ui.cliente

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.DesktopWindows
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Title
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.galindo.ayudacampus.data.model.Prioridad
import com.galindo.ayudacampus.ui.components.AyudaTextField
import com.galindo.ayudacampus.viewmodel.NuevoTicketViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevoTicketScreen(
    viewModel: NuevoTicketViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val categoriasActivas by viewModel.categoriasActivas.collectAsState()
    val categoriaSeleccionada by viewModel.categoriaSeleccionada.collectAsState()
    val titulo by viewModel.titulo.collectAsState()
    val ubicacion by viewModel.ubicacion.collectAsState()
    val prioridad by viewModel.prioridad.collectAsState()
    val descripcion by viewModel.descripcion.collectAsState()

    val errorCategoria by viewModel.errorCategoria.collectAsState()
    val errorTitulo by viewModel.errorTitulo.collectAsState()
    val errorUbicacion by viewModel.errorUbicacion.collectAsState()
    val errorDescripcion by viewModel.errorDescripcion.collectAsState()
    val isEnviando by viewModel.isEnviando.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Reportar Incidencia",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Selector visual de Categorías
            Column {
                Text(
                    text = "Categoría",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categoriasActivas.forEach { cat ->
                        val selected = (categoriaSeleccionada?.id == cat.id)
                        FilterChip(
                            selected = selected,
                            onClick = { viewModel.onCategoriaSeleccionada(cat) },
                            label = { Text(cat.nombre) },
                            leadingIcon = {
                                Icon(
                                    imageVector = obtenerIconoCategoria(cat.nombre),
                                    contentDescription = cat.nombre,
                                    modifier = Modifier.size(18.dp)
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                                selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                }

                errorCategoria?.let { msg ->
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = msg,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // 2. Campo Título
            Column {
                AyudaTextField(
                    value = titulo,
                    onValueChange = viewModel::onTituloCambiado,
                    label = "Título de la incidencia",
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Title,
                            contentDescription = "Título"
                        )
                    },
                    isError = errorTitulo != null
                )

                errorTitulo?.let { msg ->
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = msg,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // 3. Campo Ubicación
            Column {
                AyudaTextField(
                    value = ubicacion,
                    onValueChange = viewModel::onUbicacionCambiada,
                    label = "Ubicación (Aula / Lab / Pabellón)",
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Ubicación"
                        )
                    },
                    isError = errorUbicacion != null
                )

                errorUbicacion?.let { msg ->
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = msg,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // 4. Selector de Prioridad (Baja, Media, Alta)
            Column {
                Text(
                    text = "Prioridad",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Prioridad.entries.forEach { p ->
                        val etiqueta = when (p) {
                            Prioridad.BAJA -> "Baja"
                            Prioridad.MEDIA -> "Media"
                            Prioridad.ALTA -> "Alta"
                        }
                        val selected = (prioridad == p)

                        FilterChip(
                            selected = selected,
                            onClick = { viewModel.onPrioridadCambiada(p) },
                            label = { Text(etiqueta) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                }
            }

            // 5. Campo Descripción
            Column {
                AyudaTextField(
                    value = descripcion,
                    onValueChange = viewModel::onDescripcionCambiada,
                    label = "Descripción detallada del problema",
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = "Descripción"
                        )
                    },
                    isError = errorDescripcion != null,
                    singleLine = false
                )

                errorDescripcion?.let { msg ->
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = msg,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 6. Botón Enviar ticket
            Button(
                onClick = {
                    viewModel.enviar(onSuccess = onBack)
                },
                enabled = !isEnviando,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isEnviando) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(
                            text = "Enviar ticket",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

private fun obtenerIconoCategoria(nombreCategoria: String): ImageVector {
    return when {
        nombreCategoria.contains("Hardware", ignoreCase = true) -> Icons.Default.DesktopWindows
        nombreCategoria.contains("Software", ignoreCase = true) -> Icons.Default.Code
        nombreCategoria.contains("Red", ignoreCase = true) || nombreCategoria.contains("Wi-Fi", ignoreCase = true) -> Icons.Default.Wifi
        nombreCategoria.contains("Cuenta", ignoreCase = true) -> Icons.Default.AccountCircle
        nombreCategoria.contains("Impresión", ignoreCase = true) -> Icons.Default.Print
        else -> Icons.Default.Category
    }
}
