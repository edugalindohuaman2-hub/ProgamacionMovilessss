package com.galindo.ayudacampus.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galindo.ayudacampus.data.repository.AyudaRepository
import com.galindo.ayudacampus.ui.components.DatoGrafico
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class PanelViewModel(
    private val repository: AyudaRepository
) : ViewModel() {

    private val todosLosTickets = repository.observarTodosLosTickets()

    // Contador de tickets Abiertos/Pendientes
    val abiertos: StateFlow<Int> = todosLosTickets
        .map { list ->
            list.count {
                it.ticket.estado.equals("ABIERTO", ignoreCase = true) ||
                        it.ticket.estado.equals("PENDIENTE", ignoreCase = true)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    // Contador de tickets En Proceso/Atendidos
    val enProceso: StateFlow<Int> = todosLosTickets
        .map { list ->
            list.count {
                it.ticket.estado.equals("EN_PROCESO", ignoreCase = true) ||
                        it.ticket.estado.equals("ATENDIDO", ignoreCase = true)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    // Contador de tickets Resueltos/Cerrados
    val resueltos: StateFlow<Int> = todosLosTickets
        .map { list ->
            list.count {
                it.ticket.estado.equals("RESUELTO", ignoreCase = true) ||
                        it.ticket.estado.equals("CERRADO", ignoreCase = true)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    // Contador de tickets de prioridad ALTA en estado ABIERTO/PENDIENTE con más de 1 hora sin atender
    val altasSinAtender: StateFlow<Int> = todosLosTickets
        .map { list ->
            val unHoraAtras = System.currentTimeMillis() - 3_600_000L
            list.count { item ->
                val t = item.ticket
                val esAlta = t.prioridad.equals("ALTA", ignoreCase = true)
                val esAbierto = t.estado.equals("ABIERTO", ignoreCase = true) || t.estado.equals("PENDIENTE", ignoreCase = true)
                esAlta && esAbierto && (t.fecha < unHoraAtras)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    // Datos para el gráfico de Dona (Tickets por Estado)
    val graficoPorEstado: StateFlow<List<DatoGrafico>> = todosLosTickets
        .map { list ->
            val numAbiertos = list.count { it.ticket.estado.equals("ABIERTO", ignoreCase = true) || it.ticket.estado.equals("PENDIENTE", ignoreCase = true) }.toFloat()
            val numEnProceso = list.count { it.ticket.estado.equals("EN_PROCESO", ignoreCase = true) || it.ticket.estado.equals("ATENDIDO", ignoreCase = true) }.toFloat()
            val numResueltos = list.count { it.ticket.estado.equals("RESUELTO", ignoreCase = true) || it.ticket.estado.equals("CERRADO", ignoreCase = true) }.toFloat()

            listOf(
                DatoGrafico("Abiertos", numAbiertos, Color(0xFF1976D2)),
                DatoGrafico("En proceso", numEnProceso, Color(0xFFF57C00)),
                DatoGrafico("Resueltos", numResueltos, Color(0xFF388E3C))
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Datos para el gráfico de Barras (Tickets por Categoría)
    val graficoPorCategoria: StateFlow<List<DatoGrafico>> = repository.contarTicketsPorCategoria()
        .map { listaConteo ->
            val colores = listOf(
                Color(0xFF00796B),
                Color(0xFF5E35B1),
                Color(0xFF0288D1),
                Color(0xFFD81B60),
                Color(0xFFF57C00)
            )

            listaConteo.mapIndexed { index, conteo ->
                DatoGrafico(
                    etiqueta = conteo.categoriaNombre,
                    valor = conteo.total.toFloat(),
                    color = colores[index % colores.size]
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Tiempo promedio de resolución de tickets cerrados
    val tiempoPromedioResolucion: StateFlow<String> = repository.obtenerTiempoPromedioResolucion()
        .map { promedioMs ->
            formatearTiempoPromedio(promedioMs)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "Sin datos"
        )

    private fun formatearTiempoPromedio(promedioMs: Double?): String {
        if (promedioMs == null || promedioMs <= 0) return "Sin datos"
        val totalSegundos = (promedioMs / 1000).toLong()
        if (totalSegundos < 60) return "< 1 min"

        val totalMinutos = totalSegundos / 60
        val dias = totalMinutos / (24 * 60)
        val horas = (totalMinutos % (24 * 60)) / 60
        val minutos = totalMinutos % 60

        return when {
            dias > 0 -> "${dias}d ${horas}h ${minutos}m"
            horas > 0 -> "${horas}h ${minutos}m"
            else -> "$minutos min"
        }
    }
}
