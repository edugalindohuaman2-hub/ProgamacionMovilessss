package com.tecsup.mibodega.ui.cliente.screens.bienvenida

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.R
import com.tecsup.mibodega.ui.theme.AzulEnlace
import com.tecsup.mibodega.ui.theme.FondoClaro
import com.tecsup.mibodega.ui.theme.VerdeBodega

@Composable
fun BienvenidaScreen(
    onRegistrarse: () -> Unit,
    onIniciarSesion: () -> Unit,
    onTerminos: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(FondoClaro, MaterialTheme.colorScheme.background),
                    endY = 900f
                )
            )
            .safeDrawingPadding()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.ilustracion_bodega),
                contentDescription = "Ilustración de la bodega",
                modifier = Modifier.size(200.dp)
            )
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = buildAnnotatedString {
                append("Mi ")
                withStyle(SpanStyle(color = VerdeBodega)) { append("Bodega") }
            },
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = "Tus productos de siempre\nen la puerta de tu casa",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.weight(1f))

        Button(
            onClick = onRegistrarse,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(27.dp),
            colors = ButtonDefaults.buttonColors(containerColor = VerdeBodega)
        ) {
            Icon(Icons.Default.Phone, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Registrarme con mi teléfono",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        Spacer(Modifier.height(12.dp))

        OutlinedButton(
            onClick = onIniciarSesion,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(27.dp)
        ) {
            Text(
                text = "Iniciar sesión",
                style = MaterialTheme.typography.titleMedium
            )
        }

        Spacer(Modifier.height(20.dp))

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Al continuar aceptas nuestros",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Términos y Condiciones",
                style = MaterialTheme.typography.bodySmall,
                color = AzulEnlace,
                modifier = Modifier.clickable(onClick = onTerminos)
            )
        }

        Spacer(Modifier.height(24.dp))
    }
}
