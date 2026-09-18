package com.example.knjizara

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.example.knjizara.navigation.AppNavHost
import com.example.knjizara.ui.theme.AppTheme


@Composable
@Preview
fun App() {
    AppTheme {
        AppNavHost()
    }
}