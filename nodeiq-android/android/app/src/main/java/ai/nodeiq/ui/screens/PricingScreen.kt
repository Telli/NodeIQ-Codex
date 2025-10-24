package ai.nodeiq.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PricingScreen(onSave: (Double, Double) -> Unit = { _, _ -> }) {
    val price = remember { mutableStateOf("0.02") }
    val minPrice = remember { mutableStateOf("0.002") }
    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(value = price.value, onValueChange = { price.value = it }, label = { Text("Price per 1k tokens") })
        OutlinedTextField(value = minPrice.value, onValueChange = { minPrice.value = it }, label = { Text("Minimum query price") }, modifier = Modifier.padding(top = 8.dp))
        Button(onClick = {
            onSave(price.value.toDoubleOrNull() ?: 0.0, minPrice.value.toDoubleOrNull() ?: 0.0)
        }, modifier = Modifier.fillMaxWidth().padding(top = 12.dp)) {
            Text("Save")
        }
    }
}
