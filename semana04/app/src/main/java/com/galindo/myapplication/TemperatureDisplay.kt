import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Button
import androidx.compose.ui.graphics.Color

@Composable
fun TemperatureDisplay() {
    var temperatura by remember { mutableStateOf(20) }

    val colorTexto = when {
        temperatura > 30 -> Color.Red
        temperatura < 10 -> Color.Blue
        else -> Color.Black
    }

    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "$temperatura °C", fontSize = 32.sp)

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { temperatura++ }) { Text("Subir") }
            Button(onClick = { temperatura-- }) { Text("Bajar") }
            Button(onClick = { temperatura = 20 }) { Text("Resetear") }

        }
    }
}