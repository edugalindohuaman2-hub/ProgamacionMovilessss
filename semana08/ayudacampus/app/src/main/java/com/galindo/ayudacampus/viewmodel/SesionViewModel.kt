package com.galindo.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galindo.ayudacampus.data.local.entity.Usuario
import com.galindo.ayudacampus.data.local.relation.TicketConDetalle
import com.galindo.ayudacampus.data.model.Rol
import com.galindo.ayudacampus.data.prefs.PreferenciasStore
import com.galindo.ayudacampus.data.repository.AyudaRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
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

    val notificaciones: StateFlow<Boolean> = preferenciasStore.notificaciones.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = true
    )

    val recibirTickets: StateFlow<Boolean> = preferenciasStore.recibirTickets.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = true
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val misTicketsEstudiante: StateFlow<List<TicketConDetalle>> = _usuarioActual.flatMapLatest { usuario ->
        if (usuario != null) {
            repository.observarTicketsPorCliente(usuario.id)
        } else {
            flowOf(emptyList())
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
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

    fun cerrarSesion(onComplete: () -> Unit = {}) {
        _usuarioActual.value = null
        _error.value = null
        viewModelScope.launch {
            preferenciasStore.cerrarSesion()
            onComplete()
        }
    }

    fun toggleModoOscuro(activo: Boolean) {
        viewModelScope.launch {
            preferenciasStore.guardarModoOscuro(activo)
        }
    }

    fun toggleNotificaciones(activo: Boolean) {
        viewModelScope.launch {
            preferenciasStore.guardarNotificaciones(activo)
        }
    }

    fun toggleRecibirTickets(activo: Boolean) {
        viewModelScope.launch {
            preferenciasStore.guardarRecibirTickets(activo)
        }
    }

    fun cambiarPerfilPractica(onSuccess: (Rol) -> Unit) {
        viewModelScope.launch {
            val usuarioActualVal = _usuarioActual.value
            val esActualmenteAdmin = usuarioActualVal?.rol.equals(Rol.ADMIN.name, ignoreCase = true)
            val rolObjetivo = if (esActualmenteAdmin) Rol.CLIENTE else Rol.ADMIN

            var usuarioObjetivo = repository.obtenerUsuarioPorRol(rolObjetivo.name)
            if (usuarioObjetivo == null) {
                val correo = if (rolObjetivo == Rol.ADMIN) "soporte@campus.test" else "juan@campus.test"
                val clave = if (rolObjetivo == Rol.ADMIN) "admin" else "1234"
                usuarioObjetivo = repository.login(correo, clave)
            }

            if (usuarioObjetivo != null) {
                _usuarioActual.value = usuarioObjetivo
                preferenciasStore.guardarSesion(usuarioObjetivo.rol, usuarioObjetivo.id)
                onSuccess(rolObjetivo)
            }
        }
    }
}
