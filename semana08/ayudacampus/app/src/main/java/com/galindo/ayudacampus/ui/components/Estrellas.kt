package com.galindo.ayudacampus.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Estrellas(
    calificacion: Int,
    onCalificacionSeleccionada: ((Int) -> Unit)? = null,
    interactivo: Boolean = true,
    tamanioIcono: Dp = 32.dp,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..5) {
            val estaSeleccionada = i <= calificacion
            val colorEstrella = if (estaSeleccionada) Color(0xFFFFB300) else MaterialTheme.colorScheme.outlineVariant

            Icon(
                imageVector = if (estaSeleccionada) Icons.Filled.Star else Icons.Outlined.Star,
                contentDescription = "Estrella $i",
                tint = colorEstrella,
                modifier = Modifier
                    .size(tamanioIcono)
                    .padding(horizontal = 2.dp)
                    .then(
                        if (interactivo && onCalificacionSeleccionada != null) {
                            Modifier.clickable { onCalificacionSeleccionada(i) }
                        } else {
                            Modifier
                        }
                    )
            )
        }
    }
}
