package com.galindo.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galindo.ayudacampus.data.local.entity.Categoria
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

    private val _busqueda = MutableStateFlow("")
    val busqueda: StateFlow<String> = _busqueda.asStateFlow()

    private val _filtroPrioridad = MutableStateFlow("Todas")
    val filtroPrioridad: StateFlow<String> = _filtroPrioridad.asStateFlow()

    private val _filtroCategoria = MutableStateFlow("Todas")
    val filtroCategoria: StateFlow<String> = _filtroCategoria.asStateFlow()

    // Categorías disponibles para el selector de filtros
    val categorias: StateFlow<List<Categoria>> = repository.observarCategorias()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Expone la lista filtrada dinámicamente mediante combine multi-criterio
    val tickets: StateFlow<List<TicketConDetalle>> = combine(
        repository.observarTodosLosTickets(),
        _filtroEstado,
        _busqueda,
        _filtroPrioridad,
        _filtroCategoria
    ) { listaTickets, filtroEst, query, prioridad, categoria ->
        listaTickets.filter { item ->
            val t = item.ticket
            val cliente = item.cliente
            val cat = item.categoria

            // 1. Filtro por Estado
            val coincideEstado = when (filtroEst) {
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

            // 2. Filtro de Búsqueda por Texto (Título, Descripción o Cliente)
            val coincideBusqueda = if (query.isBlank()) {
                true
            } else {
                val q = query.trim()
                t.titulo.contains(q, ignoreCase = true) ||
                        t.descripcion.contains(q, ignoreCase = true) ||
                        cliente.nombre.contains(q, ignoreCase = true)
            }

            // 3. Filtro por Prioridad
            val coincidePrioridad = if (prioridad.equals("Todas", ignoreCase = true)) {
                true
            } else {
                t.prioridad.equals(prioridad, ignoreCase = true)
            }

            // 4. Filtro por Categoría
            val coincideCategoria = if (categoria.equals("Todas", ignoreCase = true)) {
                true
            } else {
                cat.nombre.equals(categoria, ignoreCase = true)
            }

            coincideEstado && coincideBusqueda && coincidePrioridad && coincideCategoria
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun onFiltroCambiado(nuevoFiltro: FiltroEstadoBandeja) {
        _filtroEstado.value = nuevoFiltro
    }

    fun onBusquedaCambiada(query: String) {
        _busqueda.value = query
    }

    fun onPrioridadSeleccionada(prioridad: String) {
        _filtroPrioridad.value = prioridad
    }

    fun onCategoriaSeleccionada(categoria: String) {
        _filtroCategoria.value = categoria
    }
}
