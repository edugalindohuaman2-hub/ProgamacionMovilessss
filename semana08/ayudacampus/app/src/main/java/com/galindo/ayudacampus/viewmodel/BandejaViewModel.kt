package com.galindo.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galindo.ayudacampus.data.local.relation.TicketConDetalle
import com.galindo.ayudacampus.data.repository.AyudaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

enum class FiltroEstadoBandeja(val etiqueta: String) {
    TODOS("Todos"),
    ABIERTOS("Abiertos"),
    EN_PROCESO("En proceso"),
    RESUELTOS("Resueltos"),
    ALTAS_URGENTES("Urgentes ALTA")
}

class BandejaViewModel(
    private val repository: AyudaRepository
) : ViewModel() {

    private val _filtroEstado = MutableStateFlow(FiltroEstadoBandeja.TODOS)
    val filtroEstado: StateFlow<FiltroEstadoBandeja> = _filtroEstado.asStateFlow()

    // Expone la lista filtrada manteniendo el orden de prioridad SQL del DAO (ALTA > MEDIA > BAJA, fecha DESC)
    val tickets: StateFlow<List<TicketConDetalle>> = combine(
        repository.observarTodosLosTickets(),
        _filtroEstado
    ) { listaTickets, filtro ->
        listaTickets.filter { item ->
            val t = item.ticket
            when (filtro) {
                FiltroEstadoBandeja.TODOS -> true
                FiltroEstadoBandeja.ABIERTOS -> t.estado.equals("ABIERTO", ignoreCase = true) ||
                        t.estado.equals("PENDIENTE", ignoreCase = true)
                FiltroEstadoBandeja.EN_PROCESO -> t.estado.equals("EN_PROCESO", ignoreCase = true) ||
                        t.estado.equals("ATENDIDO", ignoreCase = true)
                FiltroEstadoBandeja.RESUELTOS -> t.estado.equals("RESUELTO", ignoreCase = true) ||
                        t.estado.equals("CERRADO", ignoreCase = true)
                FiltroEstadoBandeja.ALTAS_URGENTES -> t.prioridad.equals("ALTA", ignoreCase = true) &&
                        (t.estado.equals("ABIERTO", ignoreCase = true) || t.estado.equals("PENDIENTE", ignoreCase = true))
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun onFiltroCambiado(nuevoFiltro: FiltroEstadoBandeja) {
        _filtroEstado.value = nuevoFiltro
    }
}
