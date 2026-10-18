package com.galindo.ayudacampus.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.galindo.ayudacampus.data.local.relation.TicketConDetalle
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TicketCard(
    item: TicketConDetalle,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    mostrarCliente: Boolean = true
) {
    val ticket = item.ticket
    val categoria = item.categoria
    val cliente = item.cliente
    val comentarios = item.comentarios

    // Formatear Fecha
    val fechaFormateada = rememberFechaFormateada(ticket.fecha)

    // Colores según Estado
    val (estadoTexto, estadoBgColor, estadoTextColor) = when (ticket.estado.uppercase()) {
        "PENDIENTE", "ABIERTO" -> Triple("Pendiente", Color(0xFFE3F2FD), Color(0xFF0D47A1))
        "EN_PROCESO" -> Triple("En proceso", Color(0xFFFFF3E0), Color(0xFFE65100))
        "ATENDIDO" -> Triple("Atendido", Color(0xFFEDE7F6), Color(0xFF4A148C))
        "RESUELTO" -> Triple("Resuelto", Color(0xE8E8F5E9), Color(0xFF1B5E20))
        "CANCELADO", "CERRADO" -> Triple("Cerrado", Color(0xFFFFEBEE), Color(0xFFB71C1C))
        else -> Triple(ticket.estado, Color(0xFFF5F5F5), Color(0xFF424242))
    }

    // Colores según Prioridad
    val (prioridadBg, prioridadText) = when (ticket.prioridad.uppercase()) {
        "ALTA" -> Pair(Color(0xFFFFCDD2), Color(0xFFB71C1C))
        "MEDIA" -> Pair(Color(0xFFFFE0B2), Color(0xFFE65100))
        else -> Pair(Color(0xFFE8F5E9), Color(0xFF2E7D32))
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Fila Superior: ID, Categoría, Estado y Prioridad
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "#${ticket.id}",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Chip Categoría
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = categoria.nombre,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Chip Prioridad
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(prioridadBg)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = ticket.prioridad,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = prioridadText
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    // Chip Estado
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(estadoBgColor)
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = estadoTexto,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = estadoTextColor
                        )
                    }
                }
            }

            // Cliente emisor si aplica
            if (mostrarCliente && cliente.nombre.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Cliente",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = cliente.nombre,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Título
            Text(
                text = ticket.titulo,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Descripción corta
            Text(
                text = ticket.descripcion,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Fila Inferior: Ubicación, Comentarios y Fecha
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
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Ubicación",
                        tint = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = ticket.ubicacion,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Comentarios
                    Icon(
                        imageVector = Icons.Default.Comment,
                        contentDescription = "Comentarios",
                        tint = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = comentarios.size.toString(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    // Fecha
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = "Fecha",
                        tint = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = fechaFormateada,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}

private fun rememberFechaFormateada(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
