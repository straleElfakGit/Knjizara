package com.example.knjizara.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.knjizara.features.auth.LoginViewModel
import com.example.knjizara.features.auth.RegisterViewModel
import com.example.knjizara.features.auth.ui.LoginScreen
import com.example.knjizara.features.auth.ui.RegisterScreen
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
fun NavGraphBuilder.authNavGraph(navController: NavController) {
    navigation<AuthGraph>(startDestination = Login) {
        composable<Login> {
            val viewModel = koinViewModel<LoginViewModel>()
            LoginScreen(
                loginViewModel = viewModel,
                onNavigateToRegister = { navController.navigate(Register) },
                onLoginSuccess = {
                    navController.navigate(MainGraph) {
                        popUpTo(AuthGraph) { inclusive = true }
                    }
                }
            )
        }
        composable<Register> {
            val viewModel = koinViewModel<RegisterViewModel>()
            RegisterScreen(
                registerViewModel = viewModel,
                onNavigateToLogin = { navController.popBackStack() },
                onRegisterSuccess = {
                    navController.navigate(MainGraph) {
                        popUpTo(AuthGraph) { inclusive = true } }
                }
            )
        }
    }
}
