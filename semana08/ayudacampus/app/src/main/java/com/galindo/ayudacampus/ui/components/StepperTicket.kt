package com.galindo.ayudacampus.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StepperTicket(
    estadoActual: String,
    modifier: Modifier = Modifier
) {
    val pasospasos = listOf("Reportado", "En proceso", "Resuelto", "Cerrado")

    val pasoActivoIndex = when (estadoActual.uppercase()) {
        "PENDIENTE", "ABIERTO" -> 0
        "EN_PROCESO", "ATENDIDO" -> 1
        "RESUELTO" -> 2
        "CERRADO", "CANCELADO" -> 3
        else -> 0
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        pasospasos.forEachIndexed { index, titulo ->
            val estaCompletado = index < pasoActivoIndex
            val esActual = index == pasoActivoIndex

            val colorCirculo = when {
                estaCompletado || esActual -> MaterialTheme.colorScheme.primary
                else -> MaterialTheme.colorScheme.outlineVariant
            }

            val colorTexto = when {
                esActual -> MaterialTheme.colorScheme.primary
                estaCompletado -> MaterialTheme.colorScheme.onSurface
                else -> MaterialTheme.colorScheme.outline
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Línea conectora izquierda
                    if (index > 0) {
                        HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            thickness = 2.dp,
                            color = if (index <= pasoActivoIndex) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                        )
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }

                    // Círculo del Paso
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(colorCirculo),
                        contentAlignment = Alignment.Center
                    ) {
                        if (estaCompletado) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                        } else {
                            Text(
                                text = "${index + 1}",
                                color = if (esActual) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Línea conectora derecha
                    if (index < pasospasos.size - 1) {
                        HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            thickness = 2.dp,
                            color = if (index < pasoActivoIndex) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                        )
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Título del Paso
                Text(
                    text = titulo,
                    fontSize = 11.sp,
                    fontWeight = if (esActual) FontWeight.Bold else FontWeight.Normal,
                    color = colorTexto,
                    textAlign = TextAlign.Center,
                    lineHeight = 13.sp
                )
            }
        }
    }
}
