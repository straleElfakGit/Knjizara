package com.example.knjizara.features.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.knjizara.features.auth.dto.LoginUserRequest
import com.example.knjizara.features.auth.dto.UserDto
import com.example.knjizara.networking.network_utils.NetworkError
import com.example.knjizara.networking.network_utils.onError
import com.example.knjizara.networking.network_utils.onSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository
): ViewModel() {
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _loggedInUser = MutableStateFlow<UserDto?>(null)
    val loggedInUser: StateFlow<UserDto?> = _loggedInUser.asStateFlow()

    fun onEmailChange(value: String) { _email.value = value }
    fun onPasswordChange(value: String) { _password.value = value }

    fun login() {
        val email = _email.value.trim()
        val password = _password.value

        if (email.isBlank() || password.isBlank()) {
            _error.value = "Unesite email i lozinku"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            clearError()

            authRepository.login(LoginUserRequest(email, password))
                .onSuccess { user -> _loggedInUser.value = user }
                .onError { error -> _error.value = error.toLoginMessage() }

            _isLoading.value = false

        }
    }

    fun clearError() { _error.value = null }
}


private fun NetworkError.toLoginMessage(): String = when (this) {
    is NetworkError.HttpError -> when (code) {
        400 -> "Neispravni podaci"
        401 -> "Pogrešan email ili lozinka"
        else -> "Greška, pokušajte ponovo"
    }
    NetworkError.NoInternet -> "Proverite internet konekciju"
    NetworkError.Serialization -> "Greška u odgovoru servera"
    NetworkError.Unknown -> "Nepoznata greška, pokušajte ponovo"
}