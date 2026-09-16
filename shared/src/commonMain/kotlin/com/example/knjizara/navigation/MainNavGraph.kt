package com.example.knjizara.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.knjizara.features.auth.SessionManager
import com.example.knjizara.ui.HomeScreen

fun NavGraphBuilder.mainNavGraph(
    navController: NavController,
    sessionManager: SessionManager,
    onLogout: () -> Unit
) {
    navigation<MainGraph>(startDestination = Home) {

        composable<Home> {
            val currentUser by sessionManager.currentUser.collectAsState()

            currentUser?.let { user ->
                HomeScreen(
                    user = user,
                    onLogout = onLogout)
            }
        }
    }
}