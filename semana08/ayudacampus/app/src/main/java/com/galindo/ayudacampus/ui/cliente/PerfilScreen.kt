package com.galindo.ayudacampus.ui.cliente

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.galindo.ayudacampus.data.model.EstadoTicket
import com.galindo.ayudacampus.data.model.Rol
import com.galindo.ayudacampus.viewmodel.SesionViewModel

// Color primario del perfil Estudiante: Cian #0891B2
private val ColorCianEstudiante = Color(0xFF0891B2)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    viewModel: SesionViewModel,
    modifier: Modifier = Modifier,
    onCerrarSesion: () -> Unit = {},
    onCambiarPerfil: (Rol) -> Unit = {}
) {
    val usuario by viewModel.usuarioActual.collectAsState()
    val misTickets by viewModel.misTicketsEstudiante.collectAsState()
    val modoOscuro by viewModel.modoOscuro.collectAsState()
    val notificaciones by viewModel.notificaciones.collectAsState()

    val totalTickets = misTickets.size
    val ticketsAbiertos = misTickets.count {
        val estado = it.ticket.estado.uppercase()
        estado == EstadoTicket.PENDIENTE.name ||
                estado == EstadoTicket.EN_PROCESO.name ||
                estado == EstadoTicket.ATENDIDO.name ||
                estado == "ABIERTO"
    }
    val ticketsResueltos = misTickets.count {
        val estado = it.ticket.estado.uppercase()
        estado == EstadoTicket.RESUELTO.name || estado == "CERRADO"
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Mi Perfil",
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Cabecera: Tarjeta de perfil con Avatar, Nombre, Correo y Chip
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(ColorCianEstudiante.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Avatar",
                            tint = ColorCianEstudiante,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = usuario?.nombre ?: "Juan Pérez",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = usuario?.correo ?: "juan@campus.test",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        AssistChip(
                            onClick = { },
                            label = { Text("Estudiante") },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = ColorCianEstudiante.copy(alpha = 0.12f),
                                labelColor = ColorCianEstudiante
                            )
                        )
                    }
                }
            }

            // 2. Fila de Métricas / Contadores (3 Tarjetas)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TarjetaMetricaEstudiante(
                    titulo = "Abiertos",
                    valor = ticketsAbiertos,
                    colorFondo = Color(0xFFFEF3C7), // Amarillo claro
                    colorTexto = Color(0xFF92400E),
                    modifier = Modifier.weight(1f)
                )

                TarjetaMetricaEstudiante(
                    titulo = "Resueltos",
                    valor = ticketsResueltos,
                    colorFondo = Color(0xFFDCFCE7), // Verde claro
                    colorTexto = Color(0xFF166534),
                    modifier = Modifier.weight(1f)
                )

                TarjetaMetricaEstudiante(
                    titulo = "Total",
                    valor = totalTickets,
                    colorFondo = Color(0xFFCFFAFE), // Cian claro
                    colorTexto = Color(0xFF155E75),
                    modifier = Modifier.weight(1f)
                )
            }

            // 3. Tarjeta interactiva "Cambiar de perfil"
            Card(
                onClick = {
                    viewModel.cambiarPerfilPractica { nuevoRol ->
                        onCambiarPerfil(nuevoRol)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = ColorCianEstudiante
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.SwapHoriz,
                        contentDescription = "Cambiar de perfil",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Cambiar de perfil",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Modo práctica: ir a vista Soporte TI",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Ir",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // 4. Sección "Preferencias"
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Preferencias",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = ColorCianEstudiante
                    )

                    // Switch Modo Oscuro
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.DarkMode,
                                contentDescription = "Modo oscuro",
                                tint = ColorCianEstudiante,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Modo oscuro",
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "Cambia el tema de la app",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Switch(
                            checked = modoOscuro,
                            onCheckedChange = { viewModel.toggleModoOscuro(it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = ColorCianEstudiante
                            )
                        )
                    }

                    // Switch Notificaciones
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notificaciones",
                                tint = ColorCianEstudiante,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Notificaciones",
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "Avisar cambios de estado",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Switch(
                            checked = notificaciones,
                            onCheckedChange = { viewModel.toggleNotificaciones(it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = ColorCianEstudiante
                            )
                        )
                    }

                    // Mensaje Informativo DataStore
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "DataStore Info",
                            tint = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Se guardan en el dispositivo con DataStore",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 5. Botón Inferior "Cerrar sesión" con borde rojo
            OutlinedButton(
                onClick = {
                    viewModel.cerrarSesion {
                        onCerrarSesion()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color(0xFFD32F2F))
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = "Cerrar sesión",
                    tint = Color(0xFFD32F2F)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Cerrar sesión",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFD32F2F)
                )
            }
        }
    }
}

@Composable
private fun TarjetaMetricaEstudiante(
    titulo: String,
    valor: Int,
    colorFondo: Color,
    colorTexto: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = colorFondo)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = valor.toString(),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = colorTexto
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = titulo,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = colorTexto
            )
        }
    }
}
