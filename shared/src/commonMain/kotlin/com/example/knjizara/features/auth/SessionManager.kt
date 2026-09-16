package com.example.knjizara.features.auth

import com.example.knjizara.features.auth.dto.UserDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SessionManager {
    private val _currentUser = MutableStateFlow<UserDto?>(null)
    val currentUser: StateFlow<UserDto?> = _currentUser.asStateFlow()

    val isLoggedIn: Boolean
        get() = _currentUser.value != null

    fun onLoginSuccess(user: UserDto) {
        _currentUser.value = user
    }

    fun logout() {
        _currentUser.value = null
    }
}