package ai.nodeiq.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun WalletScreen(balance: Double = 0.0, onTopUp: () -> Unit = {}) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Balance: $balance credits")
        Button(onClick = onTopUp, modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
            Text("Top up")
        }
    }
}
