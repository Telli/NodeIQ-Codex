package ai.nodeiq.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProviderScreen() {
    val providerEnabled = remember { mutableStateOf(false) }
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Provider mode")
        Switch(checked = providerEnabled.value, onCheckedChange = { providerEnabled.value = it })
        Button(onClick = { /* launch settings */ }, modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
            Text("Configure Pricing")
        }
    }
}
