package com.galindo.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galindo.ayudacampus.data.local.entity.Categoria
import com.galindo.ayudacampus.data.model.Prioridad
import com.galindo.ayudacampus.data.repository.AyudaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NuevoTicketViewModel(
    private val repository: AyudaRepository,
    private val clienteId: Int
) : ViewModel() {

    // Opciones precargadas por categoría
    private val laboratorios = listOf(
        "Lab 1 - Pabellón A",
        "Lab 2 - Pabellón A",
        "Lab 3 - Pabellón B",
        "Lab 4 - Pabellón B",
        "Lab 5 - Pabellón C"
    )

    private val aulas = listOf(
        "Aula 101 - Pabellón A",
        "Aula 102 - Pabellón A",
        "Aula 201 - Pabellón B",
        "Aula 202 - Pabellón B",
        "Aula 305 - Pabellón C"
    )

    private val pabellones = listOf(
        "Pabellón A - Piso 1",
        "Pabellón A - Piso 2",
        "Pabellón B - Piso 1",
        "Pabellón B - Piso 2",
        "Pabellón C - Piso 3"
    )

    private val opcionesGenerales = listOf(
        "Lab 1 - Pabellón A",
        "Lab 2 - Pabellón A",
        "Aula 101 - Pabellón A",
        "Aula 201 - Pabellón B",
        "Pabellón A - Piso 1"
    )

    // Categorías activas desde el repositorio
    val categoriasActivas: StateFlow<List<Categoria>> = repository.observarCategoriasActivas()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Estados del formulario
    private val _categoriaSeleccionada = MutableStateFlow<Categoria?>(null)
    val categoriaSeleccionada: StateFlow<Categoria?> = _categoriaSeleccionada.asStateFlow()

    private val _titulo = MutableStateFlow("")
    val titulo: StateFlow<String> = _titulo.asStateFlow()

    private val _ubicacion = MutableStateFlow("")
    val ubicacion: StateFlow<String> = _ubicacion.asStateFlow()

    private val _sugerenciasUbicacion = MutableStateFlow<List<String>>(opcionesGenerales)
    val sugerenciasUbicacion: StateFlow<List<String>> = _sugerenciasUbicacion.asStateFlow()

    private val _prioridad = MutableStateFlow(Prioridad.MEDIA)
    val prioridad: StateFlow<Prioridad> = _prioridad.asStateFlow()

    private val _descripcion = MutableStateFlow("")
    val descripcion: StateFlow<String> = _descripcion.asStateFlow()

    // Estados de error independientes por campo
    private val _errorCategoria = MutableStateFlow<String?>(null)
    val errorCategoria: StateFlow<String?> = _errorCategoria.asStateFlow()

    private val _errorTitulo = MutableStateFlow<String?>(null)
    val errorTitulo: StateFlow<String?> = _errorTitulo.asStateFlow()

    private val _errorUbicacion = MutableStateFlow<String?>(null)
    val errorUbicacion: StateFlow<String?> = _errorUbicacion.asStateFlow()

    private val _errorDescripcion = MutableStateFlow<String?>(null)
    val errorDescripcion: StateFlow<String?> = _errorDescripcion.asStateFlow()

    private val _isEnviando = MutableStateFlow(false)
    val isEnviando: StateFlow<Boolean> = _isEnviando.asStateFlow()

    fun onCategoriaSeleccionada(categoria: Categoria) {
        _categoriaSeleccionada.value = categoria
        _errorCategoria.value = null
    }

    fun onTituloCambiado(nuevoTitulo: String) {
        _titulo.value = nuevoTitulo
        _errorTitulo.value = null
    }

    fun onUbicacionCambiada(nuevaUbicacion: String) {
        _ubicacion.value = nuevaUbicacion
        _errorUbicacion.value = null

        val query = nuevaUbicacion.trim().lowercase()
            .replace("ó", "o")
            .replace("á", "a")
            .replace("é", "e")
            .replace("í", "i")
            .replace("ú", "u")

        _sugerenciasUbicacion.value = when {
            query.contains("lab") -> laboratorios
            query.contains("aula") -> aulas
            query.contains("pabellon") -> pabellones
            else -> opcionesGenerales
        }
    }

    fun seleccionarSugerenciaUbicacion(sugerencia: String) {
        onUbicacionCambiada(sugerencia)
    }

    fun onPrioridadCambiada(nuevaPrioridad: Prioridad) {
        _prioridad.value = nuevaPrioridad
    }

    fun onDescripcionCambiada(nuevaDescripcion: String) {
        _descripcion.value = nuevaDescripcion
        _errorDescripcion.value = null
    }

    fun enviar(onSuccess: () -> Unit) {
        var valido = true

        val cat = _categoriaSeleccionada.value
        if (cat == null) {
            _errorCategoria.value = "Debe seleccionar una categoría activa."
            valido = false
        } else {
            _errorCategoria.value = null
        }

        val tit = _titulo.value.trim()
        if (tit.length < 5) {
            _errorTitulo.value = "El título debe tener al menos 5 caracteres."
            valido = false
        } else {
            _errorTitulo.value = null
        }

        val ubi = _ubicacion.value.trim()
        if (ubi.isEmpty()) {
            _errorUbicacion.value = "Ingrese la ubicación (aula, lab, pabellón)."
            valido = false
        } else {
            _errorUbicacion.value = null
        }

        val desc = _descripcion.value.trim()
        if (desc.length < 10) {
            _errorDescripcion.value = "La descripción debe tener al menos 10 caracteres."
            valido = false
        } else {
            _errorDescripcion.value = null
        }

        if (!valido) return

        viewModelScope.launch {
            _isEnviando.value = true
            val idNuevo = repository.crearTicket(
                clienteId = clienteId,
                categoriaId = cat!!.id,
                titulo = tit,
                descripcion = desc,
                ubicacion = ubi,
                prioridad = _prioridad.value.name
            )
            _isEnviando.value = false

            if (idNuevo > 0) {
                onSuccess()
            }
        }
    }
}
