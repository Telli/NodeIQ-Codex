package ai.nodeiq.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ai.nodeiq.ui.screens.ChatScreen
import ai.nodeiq.ui.screens.HomeScreen
import ai.nodeiq.ui.screens.OnboardingScreen
import ai.nodeiq.ui.theme.NodeIqTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NodeIqTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    NodeIqNavHost()
                }
            }
        }
    }
}

@Composable
private fun NodeIqNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = "onboarding") {
        composable("onboarding") {
            OnboardingScreen(onContinue = { navController.navigate("home") })
        }
        composable("home") {
            HomeScreen(navController)
        }
        composable("chat/{peerId}") {
            val peerId = it.arguments?.getString("peerId") ?: ""
            ChatScreen(peerId = peerId)
        }
    }
}
