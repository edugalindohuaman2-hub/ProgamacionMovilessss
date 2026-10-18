package com.galindo.ayudacampus.ui.cliente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.galindo.ayudacampus.data.model.Rol
import com.galindo.ayudacampus.ui.components.AyudaTextField
import com.galindo.ayudacampus.viewmodel.SesionViewModel

@Composable
fun LoginScreen(
    viewModel: SesionViewModel,
    onLoginSuccess: (Rol) -> Unit,
    modifier: Modifier = Modifier
) {
    var rolSeleccionado by remember { mutableStateOf(Rol.CLIENTE) }
    var correo by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val usuarioActual by viewModel.usuarioActual.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(usuarioActual) {
        usuarioActual?.let { usuario ->
            val rol = if (usuario.rol.equals(Rol.ADMIN.name, ignoreCase = true)) Rol.ADMIN else Rol.CLIENTE
            onLoginSuccess(rol)
        }
    }

    // Dynamic color scheme based on selected role
    val dynamicPrimaryColor = if (rolSeleccionado == Rol.CLIENTE) {
        Color(0xFF1976D2) // Estudiante - Blue
    } else {
        Color(0xFF00796B) // Soporte TI - Teal
    }

    val dynamicColorScheme = MaterialTheme.colorScheme.copy(
        primary = dynamicPrimaryColor,
        primaryContainer = if (rolSeleccionado == Rol.CLIENTE) Color(0xFFE3F2FD) else Color(0xFFE0F2F1),
        onPrimary = Color.White
    )

    MaterialTheme(colorScheme = dynamicColorScheme) {
        Surface(
            modifier = modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Header Logo / Icon
                Icon(
                    imageVector = if (rolSeleccionado == Rol.CLIENTE) Icons.Default.Person else Icons.Default.SupportAgent,
                    contentDescription = "Icono Rol",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(72.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "AyudaCampus",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = "Sistema de Gestión de Incidencias",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(32.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Seleccione su Rol",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Role Selector ("Estudiante" / "Soporte TI")
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            FilterChip(
                                selected = (rolSeleccionado == Rol.CLIENTE),
                                onClick = {
                                    rolSeleccionado = Rol.CLIENTE
                                    viewModel.limpiarError()
                                },
                                label = { Text("Estudiante") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                                    selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimary
                                )
                            )

                            FilterChip(
                                selected = (rolSeleccionado == Rol.ADMIN),
                                onClick = {
                                    rolSeleccionado = Rol.ADMIN
                                    viewModel.limpiarError()
                                },
                                label = { Text("Soporte TI") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.SupportAgent,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                                    selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimary
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Campo Correo con AyudaTextField
                        AyudaTextField(
                            value = correo,
                            onValueChange = {
                                correo = it
                                if (error != null) viewModel.limpiarError()
                            },
                            label = "Correo Electrónico",
                            modifier = Modifier.fillMaxWidth(),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Email,
                                    contentDescription = "Correo"
                                )
                            },
                            isError = error != null
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Campo Contraseña con AyudaTextField
                        AyudaTextField(
                            value = clave,
                            onValueChange = {
                                clave = it
                                if (error != null) viewModel.limpiarError()
                            },
                            label = "Contraseña",
                            modifier = Modifier.fillMaxWidth(),
                            isPassword = true,
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = "Contraseña"
                                )
                            },
                            trailingIcon = {
                                val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(imageVector = image, contentDescription = "Mostrar contraseña")
                                }
                            },
                            isError = error != null
                        )

                        // Mensaje de Error
                        error?.let { msg ->
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = msg,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Botón Ingresar
                        Button(
                            onClick = {
                                viewModel.login(correo, clave, rolSeleccionado)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "Ingresar",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
