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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

enum class FiltroTicket(val etiqueta: String) {
    TODOS("Todos"),
    ABIERTOS("Abiertos"),
    EN_PROCESO("En proceso"),
    RESUELTOS("Resueltos")
}

class MisTicketsViewModel(
    private val repository: AyudaRepository,
    private val clienteId: Int
) : ViewModel() {

    private val _filtroSeleccionado = MutableStateFlow(FiltroTicket.TODOS)
    val filtroSeleccionado: StateFlow<FiltroTicket> = _filtroSeleccionado.asStateFlow()

    private val _busqueda = MutableStateFlow("")
    val busqueda: StateFlow<String> = _busqueda.asStateFlow()

    // Exponemos un único StateFlow con la lista filtrada y buscada
    val tickets: StateFlow<List<TicketConDetalle>> = combine(
        repository.observarTicketsPorCliente(clienteId),
        _filtroSeleccionado,
        _busqueda
    ) { listaTickets, filtro, query ->
        listaTickets.filter { item ->
            val t = item.ticket

            // 1. Filtrado por estado
            val cumpleFiltro = when (filtro) {
                FiltroTicket.TODOS -> true
                FiltroTicket.ABIERTOS -> t.estado.equals("PENDIENTE", ignoreCase = true) ||
                        t.estado.equals("ABIERTO", ignoreCase = true)
                FiltroTicket.EN_PROCESO -> t.estado.equals("EN_PROCESO", ignoreCase = true) ||
                        t.estado.equals("ATENDIDO", ignoreCase = true)
                FiltroTicket.RESUELTOS -> t.estado.equals("RESUELTO", ignoreCase = true) ||
                        t.estado.equals("CANCELADO", ignoreCase = true) ||
                        t.estado.equals("CERRADO", ignoreCase = true)
            }

            // 2. Búsqueda por texto (coincidencia en título o ID de ticket)
            val queryClean = query.trim()
            val cumpleBusqueda = if (queryClean.isEmpty()) {
                true
            } else {
                t.titulo.contains(queryClean, ignoreCase = true) ||
                        t.id.toString().contains(queryClean) ||
                        "#${t.id}".contains(queryClean, ignoreCase = true) ||
                        item.categoria.nombre.contains(queryClean, ignoreCase = true)
            }

            cumpleFiltro && cumpleBusqueda
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Cantidad de tickets en estado abierto/pendiente para el saludo inicial
    val cantidadAbiertos: StateFlow<Int> = repository.observarTicketsPorCliente(clienteId)
        .map { list ->
            list.count { item ->
                val estado = item.ticket.estado
                estado.equals("PENDIENTE", ignoreCase = true) ||
                        estado.equals("ABIERTO", ignoreCase = true) ||
                        estado.equals("EN_PROCESO", ignoreCase = true) ||
                        estado.equals("ATENDIDO", ignoreCase = true)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    fun onFiltroCambiado(nuevoFiltro: FiltroTicket) {
        _filtroSeleccionado.value = nuevoFiltro
    }

    fun onBusquedaCambiada(nuevaBusqueda: String) {
        _busqueda.value = nuevaBusqueda
    }
}
