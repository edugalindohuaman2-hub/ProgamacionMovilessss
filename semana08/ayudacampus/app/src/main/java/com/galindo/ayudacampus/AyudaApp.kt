package com.galindo.ayudacampus

import android.app.Application
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.galindo.ayudacampus.data.local.AyudaDatabase
import com.galindo.ayudacampus.data.local.DatosIniciales
import com.galindo.ayudacampus.data.prefs.PreferenciasStore
import com.galindo.ayudacampus.data.repository.AyudaRepository
import com.galindo.ayudacampus.data.repository.AyudaRepositoryRoom
import com.galindo.ayudacampus.navigation.NavAyuda
import com.galindo.ayudacampus.ui.theme.AyudacampusTheme
import com.galindo.ayudacampus.viewmodel.SesionViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AyudaApp : Application() {

    val database: AyudaDatabase by lazy {
        AyudaDatabase.getDatabase(this)
    }

    val repository: AyudaRepository by lazy {
        AyudaRepositoryRoom(
            usuarioDao = database.usuarioDao(),
            categoriaDao = database.categoriaDao(),
            ticketDao = database.ticketDao()
        )
    }

    val preferenciasStore: PreferenciasStore by lazy {
        PreferenciasStore(this)
    }

    val sesionViewModelFactory: ViewModelProvider.Factory by lazy {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SesionViewModel(repository, preferenciasStore) as T
            }
        }
    }

    fun misTicketsViewModelFactory(clienteId: Int): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return com.galindo.ayudacampus.viewmodel.MisTicketsViewModel(repository, clienteId) as T
            }
        }
    }

    fun nuevoTicketViewModelFactory(clienteId: Int): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return com.galindo.ayudacampus.viewmodel.NuevoTicketViewModel(repository, clienteId) as T
            }
        }
    }

    fun ticketDetalleViewModelFactory(ticketId: Int, clienteId: Int): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return com.galindo.ayudacampus.viewmodel.TicketDetalleViewModel(repository, ticketId, clienteId) as T
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        CoroutineScope(Dispatchers.IO).launch {
            DatosIniciales.sembrar(database)
        }
    }
}

@Composable
fun AyudaApp.AppContent() {
    val sesionViewModel: SesionViewModel = viewModel(factory = sesionViewModelFactory)
    val modoOscuro by sesionViewModel.modoOscuro.collectAsState()

    AyudacampusTheme(darkTheme = modoOscuro) {
        Surface(modifier = Modifier.fillMaxSize()) {
            NavAyuda(
                sesionViewModel = sesionViewModel,
                preferenciasStore = preferenciasStore
            )
        }
    }
}
