package com.galindo.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galindo.ayudacampus.data.local.entity.Categoria
import com.galindo.ayudacampus.data.repository.AyudaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CategoriasViewModel(
    private val repository: AyudaRepository
) : ViewModel() {

    val categorias: StateFlow<List<Categoria>> = repository.observarCategorias()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _categoriaEnEdicion = MutableStateFlow<Categoria?>(null)
    val categoriaEnEdicion: StateFlow<Categoria?> = _categoriaEnEdicion.asStateFlow()

    private val _nombre = MutableStateFlow("")
    val nombre: StateFlow<String> = _nombre.asStateFlow()

    private val _descripcion = MutableStateFlow("")
    val descripcion: StateFlow<String> = _descripcion.asStateFlow()

    private val _icono = MutableStateFlow("tag")
    val icono: StateFlow<String> = _icono.asStateFlow()

    private val _activa = MutableStateFlow(true)
    val activa: StateFlow<Boolean> = _activa.asStateFlow()

    private val _mensajeError = MutableStateFlow<String?>(null)
    val mensajeError: StateFlow<String?> = _mensajeError.asStateFlow()

    private val _mensajeExito = MutableStateFlow<String?>(null)
    val mensajeExito: StateFlow<String?> = _mensajeExito.asStateFlow()

    fun cargarParaEditar(categoria: Categoria?) {
        _categoriaEnEdicion.value = categoria
        if (categoria != null) {
            _nombre.value = categoria.nombre
            _descripcion.value = categoria.descripcion
            _icono.value = categoria.icono
            _activa.value = categoria.activa
        } else {
            limpiarFormulario()
        }
        _mensajeError.value = null
        _mensajeExito.value = null
    }

    fun onNombreCambiado(nuevoNombre: String) {
        _nombre.value = nuevoNombre
        _mensajeError.value = null
    }

    fun onDescripcionCambiada(nuevaDescripcion: String) {
        _descripcion.value = nuevaDescripcion
    }

    fun onIconoCambiado(nuevoIcono: String) {
        _icono.value = nuevoIcono
    }

    fun onActivaCambiado(nuevaActiva: Boolean) {
        _activa.value = nuevaActiva
    }

    fun limpiarFormulario() {
        _categoriaEnEdicion.value = null
        _nombre.value = ""
        _descripcion.value = ""
        _icono.value = "tag"
        _activa.value = true
        _mensajeError.value = null
        _mensajeExito.value = null
    }

    fun guardar(categoria: Categoria) {
        if (categoria.nombre.isBlank()) {
            _mensajeError.value = "El nombre de la categoría no puede estar vacío."
            return
        }

        viewModelScope.launch {
            repository.guardarCategoria(categoria)
            _mensajeExito.value = "Categoría guardada con éxito."
            limpiarFormulario()
        }
    }

    fun eliminar(categoria: Categoria) {
        viewModelScope.launch {
            val totalTickets = repository.contarTicketsPorCategoriaId(categoria.id)
            if (totalTickets > 0) {
                _mensajeError.value = "No se puede eliminar la categoría '${categoria.nombre}' porque está asociada a $totalTickets ticket(s) existente(s)."
                return@launch
            }

            repository.eliminarCategoria(categoria)
            _mensajeExito.value = "Categoría '${categoria.nombre}' eliminada correctamente."
            limpiarFormulario()
        }
    }

    fun limpiarMensajes() {
        _mensajeError.value = null
        _mensajeExito.value = null
    }
}
