package com.galindo.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galindo.ayudacampus.data.local.relation.TicketConDetalle
import com.galindo.ayudacampus.data.repository.AyudaRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TicketDetalleViewModel(
    private val repository: AyudaRepository,
    private val ticketId: Int,
    private val clienteId: Int
) : ViewModel() {

    // Observa en tiempo real el ticket seleccionado por su ID
    val ticket: StateFlow<TicketConDetalle?> = repository.observarTicketPorId(ticketId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun comentar(texto: String) {
        val t = ticket.value?.ticket ?: return
        val estado = t.estado.uppercase()

        // No se puede comentar si está CERRADO o CANCELADO, ni si el texto está vacío
        if (estado == "CERRADO" || estado == "CANCELADO") return
        if (texto.isBlank()) return

        viewModelScope.launch {
            val autor = repository.obtenerUsuarioPorId(clienteId) ?: return@launch
            repository.agregarComentario(ticketId, autor, texto.trim())
        }
    }

    fun cerrar(calificacion: Int) {
        val t = ticket.value?.ticket ?: return

        // Solo se permite cerrar si el ticket está en estado RESUELTO
        if (!t.estado.equals("RESUELTO", ignoreCase = true)) return
        if (calificacion !in 1..5) return

        viewModelScope.launch {
            repository.cerrarConCalificacion(ticketId, calificacion)
        }
    }

    fun reabrir() {
        val t = ticket.value?.ticket ?: return

        // Solo se permite reabrir si el ticket está en estado RESUELTO
        if (!t.estado.equals("RESUELTO", ignoreCase = true)) return

        viewModelScope.launch {
            repository.reabrir(ticketId)
        }
    }
}
