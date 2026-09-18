package com.example.knjizara.features.auth.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.knjizara.features.auth.LoginViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel = koinViewModel<LoginViewModel>(),
    onLoginSuccess: () -> Unit = {},
    onNavigateToRegister: () -> Unit = {}
) {
    val email by loginViewModel.email.collectAsState()
    val password by loginViewModel.password.collectAsState()
    val isLoading by loginViewModel.isLoading.collectAsState()
    val error by loginViewModel.error.collectAsState()
    val user by loginViewModel.loggedInUser.collectAsState()

    LaunchedEffect(user) {
        user?.let {
            onLoginSuccess()
        }
    }

    LaunchedEffect(error) {
        error?.let {

        }
    }

    val focusManager = LocalFocusManager.current
    Box(modifier = modifier.fillMaxSize())
    {
        Column(
            modifier = modifier.fillMaxSize()
                .clickable(indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    focusManager.clearFocus()
                },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(text = "Prijava",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(text = "Dobrodošli nazad u aplikaciju!",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height((50.dp)))

            CustomTextField(
                value = email,
                onValueChange = { loginViewModel.onEmailChange(it) },
                label = "E-mail",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(modifier = Modifier.height((20.dp)))

            CustomTextField(
                value = password,
                onValueChange = { loginViewModel.onPasswordChange(it) },
                label = "Lozinka",
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height((40.dp)))

            Button(
                onClick = { loginViewModel.login() },
                enabled = !isLoading
            ){
                Text(text="Prijava")
            }
            if (isLoading) {
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator()
            }

            if (error != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = error!!,
                    color = MaterialTheme.colorScheme.error
                )
            }
            Spacer(modifier = Modifier.height((10.dp)))
            Text(text="Nemate nalog? Registrujte se",
                modifier = Modifier.clickable {
                    onNavigateToRegister()
                }
            )
        }
    }
}