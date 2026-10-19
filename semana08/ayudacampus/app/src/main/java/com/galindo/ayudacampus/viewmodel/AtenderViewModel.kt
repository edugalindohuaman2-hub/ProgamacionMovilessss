package com.galindo.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galindo.ayudacampus.data.local.relation.TicketConDetalle
import com.galindo.ayudacampus.data.repository.AyudaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AtenderViewModel(
    private val repository: AyudaRepository,
    private val ticketId: Int,
    private val soporteUsuarioId: Int
) : ViewModel() {

    val ticket: StateFlow<TicketConDetalle?> = repository.observarTicketPorId(ticketId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    private val _estadoSeleccionado = MutableStateFlow<String?>(null)
    val estadoSeleccionado: StateFlow<String?> = _estadoSeleccionado.asStateFlow()

    private val _prioridadSeleccionada = MutableStateFlow<String?>(null)
    val prioridadSeleccionada: StateFlow<String?> = _prioridadSeleccionada.asStateFlow()

    private val _nuevoComentario = MutableStateFlow("")
    val nuevoComentario: StateFlow<String> = _nuevoComentario.asStateFlow()

    private val _guardando = MutableStateFlow(false)
    val guardando: StateFlow<Boolean> = _guardando.asStateFlow()

    private val _mensajeExito = MutableStateFlow<String?>(null)
    val mensajeExito: StateFlow<String?> = _mensajeExito.asStateFlow()

    init {
        viewModelScope.launch {
            ticket.collect { item ->
                if (item != null) {
                    if (_estadoSeleccionado.value == null) {
                        _estadoSeleccionado.value = item.ticket.estado.uppercase()
                    }
                    if (_prioridadSeleccionada.value == null) {
                        _prioridadSeleccionada.value = item.ticket.prioridad.uppercase()
                    }
                }
            }
        }
    }

    fun seleccionarEstado(nuevoEstado: String) {
        val estadoUpper = nuevoEstado.uppercase()
        // La opción CERRADO o CANCELADO no se puede seleccionar desde soporte
        if (estadoUpper == "CERRADO" || estadoUpper == "CANCELADO") return
        _estadoSeleccionado.value = estadoUpper
    }

    fun seleccionarPrioridad(nuevaPrioridad: String) {
        _prioridadSeleccionada.value = nuevaPrioridad.uppercase()
    }

    fun onComentarioCambiado(texto: String) {
        _nuevoComentario.value = texto
    }

    fun guardarCambios(onExito: () -> Unit = {}) {
        val t = ticket.value?.ticket ?: return
        val nuevoEstado = _estadoSeleccionado.value ?: t.estado
        val nuevaPrioridad = _prioridadSeleccionada.value ?: t.prioridad
        val comentarioTexto = _nuevoComentario.value.trim()

        viewModelScope.launch {
            _guardando.value = true

            // 1. Modificar estado si cambió
            if (!nuevoEstado.equals(t.estado, ignoreCase = true)) {
                repository.cambiarEstado(ticketId, nuevoEstado)
            }

            // 2. Modificar prioridad si cambió
            if (!nuevaPrioridad.equals(t.prioridad, ignoreCase = true)) {
                repository.cambiarPrioridad(ticketId, nuevaPrioridad)
            }

            // 3. Enviar respuesta / comentario si no está vacío
            if (comentarioTexto.isNotBlank()) {
                val autor = repository.obtenerUsuarioPorId(soporteUsuarioId)
                if (autor != null) {
                    repository.agregarComentario(ticketId, autor, comentarioTexto)
                }
                _nuevoComentario.value = ""
            }

            _guardando.value = false
            _mensajeExito.value = "Cambios guardados correctamente"
            onExito()
        }
    }

    fun limpiarMensajeExito() {
        _mensajeExito.value = null
    }
}
