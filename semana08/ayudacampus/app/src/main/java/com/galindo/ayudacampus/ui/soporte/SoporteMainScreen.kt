package com.galindo.ayudacampus.ui.soporte

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.galindo.ayudacampus.viewmodel.BandejaViewModel
import com.galindo.ayudacampus.viewmodel.FiltroEstadoBandeja
import com.galindo.ayudacampus.viewmodel.PanelViewModel

@Composable
fun SoporteMainScreen(
    panelViewModel: PanelViewModel,
    bandejaViewModel: BandejaViewModel,
    modifier: Modifier = Modifier,
    onTicketClick: (Int) -> Unit = {}
) {
    var pestanaSeleccionada by remember { mutableIntStateOf(0) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = (pestanaSeleccionada == 0),
                    onClick = { pestanaSeleccionada = 0 },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Dashboard,
                            contentDescription = "Panel"
                        )
                    },
                    label = { Text("Panel") }
                )

                NavigationBarItem(
                    selected = (pestanaSeleccionada == 1),
                    onClick = { pestanaSeleccionada = 1 },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Inbox,
                            contentDescription = "Bandeja"
                        )
                    },
                    label = { Text("Bandeja") }
                )
            }
        }
    ) { innerPadding ->
        when (pestanaSeleccionada) {
            0 -> PanelScreen(
                viewModel = panelViewModel,
                onVerAltasUrgentes = {
                    bandejaViewModel.onFiltroCambiado(FiltroEstadoBandeja.ALTAS_URGENTES)
                    pestanaSeleccionada = 1
                },
                modifier = Modifier.padding(innerPadding)
            )
            1 -> BandejaScreen(
                viewModel = bandejaViewModel,
                onTicketClick = onTicketClick,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
