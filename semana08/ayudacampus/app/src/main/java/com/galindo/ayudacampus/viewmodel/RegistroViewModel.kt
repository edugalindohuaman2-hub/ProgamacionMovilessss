package com.galindo.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galindo.ayudacampus.data.repository.AyudaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegistroViewModel(
    private val repository: AyudaRepository
) : ViewModel() {

    private val _nombre = MutableStateFlow("")
    val nombre: StateFlow<String> = _nombre.asStateFlow()

    private val _correo = MutableStateFlow("")
    val correo: StateFlow<String> = _correo.asStateFlow()

    private val _clave = MutableStateFlow("")
    val clave: StateFlow<String> = _clave.asStateFlow()

    private val _errorNombre = MutableStateFlow<String?>(null)
    val errorNombre: StateFlow<String?> = _errorNombre.asStateFlow()

    private val _errorCorreo = MutableStateFlow<String?>(null)
    val errorCorreo: StateFlow<String?> = _errorCorreo.asStateFlow()

    private val _errorClave = MutableStateFlow<String?>(null)
    val errorClave: StateFlow<String?> = _errorClave.asStateFlow()

    private val _mensajeGeneral = MutableStateFlow<String?>(null)
    val mensajeGeneral: StateFlow<String?> = _mensajeGeneral.asStateFlow()

    private val _isCargando = MutableStateFlow(false)
    val isCargando: StateFlow<Boolean> = _isCargando.asStateFlow()

    fun onNombreCambiado(texto: String) {
        _nombre.value = texto
        _errorNombre.value = null
        _mensajeGeneral.value = null
    }

    fun onCorreoCambiado(texto: String) {
        _correo.value = texto
        _errorCorreo.value = null
        _mensajeGeneral.value = null
    }

    fun onClaveCambiada(texto: String) {
        _clave.value = texto
        _errorClave.value = null
        _mensajeGeneral.value = null
    }

    fun registrar(onExito: () -> Unit) {
        val nom = _nombre.value.trim()
        val cor = _correo.value.trim()
        val cla = _clave.value.trim()

        var valido = true

        if (nom.isEmpty()) {
            _errorNombre.value = "Ingrese su nombre completo."
            valido = false
        }

        val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
        if (cor.isEmpty()) {
            _errorCorreo.value = "Ingrese su correo institucional."
            valido = false
        } else if (!emailRegex.matches(cor)) {
            _errorCorreo.value = "Ingrese un correo electrónico válido."
            valido = false
        }

        if (cla.isEmpty()) {
            _errorClave.value = "Ingrese una contraseña."
            valido = false
        } else if (cla.length < 4) {
            _errorClave.value = "La contraseña debe tener al menos 4 caracteres."
            valido = false
        }

        if (!valido) return

        viewModelScope.launch {
            _isCargando.value = true
            val resultado = repository.registrarEstudiante(nom, cor, cla)
            _isCargando.value = false

            resultado.onSuccess {
                onExito()
            }.onFailure { ex ->
                _mensajeGeneral.value = ex.message ?: "Ocurrió un error al registrarse."
            }
        }
    }

    fun limpiarMensajes() {
        _errorNombre.value = null
        _errorCorreo.value = null
        _errorClave.value = null
        _mensajeGeneral.value = null
    }
}
