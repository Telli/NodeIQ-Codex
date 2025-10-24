package ai.nodeiq.ui

import ai.nodeiq.R
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { NodeIqApp() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NodeIqApp() {
    val navController = rememberNavController()
    val backStack by navController.currentBackStackEntryAsState()
    val currentRoute = backStack?.destination?.route ?: Routes.Peers

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = if (currentRoute == Routes.Chat) R.string.chat_title else R.string.peer_browser_title) })
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.Peers,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.Peers) {
                PeerBrowserScreen(onPeerSelected = { agentId ->
                    navController.navigate("${Routes.Chat}/$agentId")
                })
            }
            composable("${Routes.Chat}/{agentId}") { entry ->
                val agentId = entry.arguments?.getString("agentId").orEmpty()
                ChatScreen(agentId = agentId)
            }
        }
    }
}

object Routes {
    const val Peers = "peers"
    const val Chat = "chat"
}
