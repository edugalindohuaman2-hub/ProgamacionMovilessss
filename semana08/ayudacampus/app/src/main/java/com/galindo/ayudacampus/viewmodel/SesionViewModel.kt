package com.galindo.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galindo.ayudacampus.data.local.entity.Usuario
import com.galindo.ayudacampus.data.model.Rol
import com.galindo.ayudacampus.data.prefs.PreferenciasStore
import com.galindo.ayudacampus.data.repository.AyudaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SesionViewModel(
    private val repository: AyudaRepository,
    private val preferenciasStore: PreferenciasStore
) : ViewModel() {

    private val _usuarioActual = MutableStateFlow<Usuario?>(null)
    val usuarioActual: StateFlow<Usuario?> = _usuarioActual.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    val modoOscuro: StateFlow<Boolean> = preferenciasStore.modoOscuro.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    init {
        viewModelScope.launch {
            preferenciasStore.usuarioId.collect { id ->
                if (id > 0) {
                    val usuario = repository.obtenerUsuarioPorId(id)
                    _usuarioActual.value = usuario
                } else {
                    _usuarioActual.value = null
                }
            }
        }
    }

    fun login(correo: String, clave: String, rolSeleccionado: Rol) {
        if (correo.isBlank() || clave.isBlank()) {
            _error.value = "Por favor, complete todos los campos."
            return
        }

        viewModelScope.launch {
            val usuario = repository.login(correo.trim(), clave.trim())
            if (usuario == null) {
                _error.value = "Credenciales incorrectas. Verifique el correo o la contraseña."
                return@launch
            }

            val coincideRol = usuario.rol.equals(rolSeleccionado.name, ignoreCase = true)
            if (!coincideRol) {
                val rolTexto = if (rolSeleccionado == Rol.CLIENTE) "Estudiante" else "Soporte TI"
                _error.value = "El usuario ingresado no corresponde al rol de $rolTexto."
                return@launch
            }

            _usuarioActual.value = usuario
            _error.value = null
            preferenciasStore.guardarSesion(usuario.rol, usuario.id)
        }
    }

    fun limpiarError() {
        _error.value = null
    }

    fun cerrarSesion() {
        viewModelScope.launch {
            preferenciasStore.cerrarSesion()
            _usuarioActual.value = null
        }
    }

    fun toggleModoOscuro(activo: Boolean) {
        viewModelScope.launch {
            preferenciasStore.guardarModoOscuro(activo)
        }
    }
}
