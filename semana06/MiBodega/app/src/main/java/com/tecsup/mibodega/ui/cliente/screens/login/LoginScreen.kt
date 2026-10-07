package com.tecsup.mibodega.ui.cliente.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(
    usuarioRegistrado: String,
    passwordRegistrada: String,
    onLoginExitoso: () -> Unit,
    onVolver: () -> Unit
) {
    var usuario by remember { mutableStateOf("edudalindo@gmail.com") }
    var password by remember { mutableStateOf("edugalindo01") }
    var errorMensaje by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Iniciar Sesión",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = usuario,
            onValueChange = { usuario = it },
            label = { Text("Correo o Teléfono") },
            placeholder = { Text("edudalindo@gmail.com") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            placeholder = { Text("edugalindo01") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (errorMensaje.isNotEmpty()) {
            Text(
                text = errorMensaje,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = {
                val userValid = usuario == "admin@gmail.com" || usuario == "edudalindo@gmail.com" || usuario == "999999999" || (usuarioRegistrado.isNotBlank() && usuario == usuarioRegistrado)
                val passValid = password == "123456" || password == "edugalindo01" || (passwordRegistrada.isNotBlank() && password == passwordRegistrada)

                if (userValid && passValid) {
                    onLoginExitoso()
                } else {
                    errorMensaje = "Credenciales incorrectas. Use edudalindo@gmail.com / edugalindo01"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ingresar")
        }

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(onClick = onVolver) {
            Text("Volver")
        }
    }
}
