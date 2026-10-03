package com.tecsup.mibodega.ui.cliente.screens.confirmacion

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.tecsup.mibodega.ui.theme.GrisBorde
import com.tecsup.mibodega.ui.theme.VerdeBodega

@Composable
fun ConfirmacionScreen(
    codigoPedido: String = "#1024",
    totalPedido: Double = 25.90,
    direccionPedido: String = "Av. Los Olivos 123",
    onIrAInicio: () -> Unit,
    onVerPedidos: () -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = "Pedido exitoso",
            tint = VerdeBodega,
            modifier = Modifier.size(96.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "¡Pedido realizado!",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = VerdeBodega,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Tu pedido está siendo preparado y será entregado pronto",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, GrisBorde),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Código", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(text = "Pedido $codigoPedido", fontWeight = FontWeight.Bold)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Total pagado", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(text = "S/ %.2f".format(totalPedido), fontWeight = FontWeight.Bold, color = VerdeBodega)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Dirección", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(text = direccionPedido, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW, "https://wa.me/51942164716?text=Hola,%20quiero%20consultar%20sobre%20mi%20pedido%20Pedido%20$codigoPedido".toUri())
                context.startActivity(intent)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(27.dp),
            colors = ButtonDefaults.buttonColors(containerColor = VerdeBodega)
        ) {
            Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Ver estado del pedido", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onPrimary)
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onVerPedidos,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(27.dp)
        ) {
            Text("Ver Mis Pedidos", style = MaterialTheme.typography.titleMedium)
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = onIrAInicio) {
            Text("Volver al inicio", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
