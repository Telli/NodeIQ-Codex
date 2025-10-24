package ai.nodeiq.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("NodeIQ") }) }
    ) { paddingValues ->
        PeersScreen(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            onPeerSelected = { navController.navigate("chat/$it") }
        )
    }
}
