package com.galindo.ayudacampus.ui.splash

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.galindo.ayudacampus.data.model.Rol
import com.galindo.ayudacampus.data.prefs.PreferenciasStore
import kotlinx.coroutines.flow.first

@Composable
fun SplashScreen(
    preferenciasStore: PreferenciasStore,
    onNavigateNext: (Rol?) -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) {
        val perfil = preferenciasStore.perfilActivo.first()
        val rolResult = when {
            perfil.equals(Rol.ADMIN.name, ignoreCase = true) || perfil == "SOPORTE" -> Rol.ADMIN
            perfil.equals(Rol.CLIENTE.name, ignoreCase = true) || perfil == "ESTUDIANTE" -> Rol.CLIENTE
            else -> null
        }
        onNavigateNext(rolResult)
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.School,
                contentDescription = "Logo AyudaCampus",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(96.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "AyudaCampus",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(32.dp))

            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
