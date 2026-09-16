package com.example.knjizara.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.knjizara.features.auth.SessionManager
import com.example.knjizara.ui.StartupState
import com.example.knjizara.ui.StartupViewModel
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val sessionManager: SessionManager = koinInject()
    val startupViewModel = koinViewModel<StartupViewModel>()
    val startupState by startupViewModel.state.collectAsStateWithLifecycle()

    when (startupState) {
        StartupState.Loading -> SplashScreen()
        else -> {
            NavHost(
                navController = navController,
                startDestination = if (startupState == StartupState.Authenticated) MainGraph else AuthGraph
            ) {
                authNavGraph(navController)
                mainNavGraph(navController, sessionManager) {
                    startupViewModel.logout()
                    navController.navigate(AuthGraph) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }
        }
    }
}