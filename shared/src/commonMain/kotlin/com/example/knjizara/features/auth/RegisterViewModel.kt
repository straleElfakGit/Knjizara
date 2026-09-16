package com.example.knjizara.features.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.knjizara.features.auth.dto.RegisterUserRequest
import com.example.knjizara.features.auth.dto.UserDto
import com.example.knjizara.networking.apis.alp_error_messages.toRegisterMessage
import com.example.knjizara.networking.network_utils.onSuccess
import com.example.knjizara.networking.network_utils.onError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val authRepository: AuthRepository
): ViewModel() {
    private val _jmbg = MutableStateFlow("")
    val jmbg: StateFlow<String> = _jmbg.asStateFlow()

    private val _firstName = MutableStateFlow("")
    val firstName: StateFlow<String> = _firstName.asStateFlow()

    private val _lastName = MutableStateFlow("")
    val lastName: StateFlow<String> = _lastName.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _registeredUser = MutableStateFlow<UserDto?>(null)
    val registeredUser: StateFlow<UserDto?> = _registeredUser.asStateFlow()

    fun onJmbgChange(value: String) { _jmbg.value = value }
    fun onFirstNameChange(value: String) { _firstName.value = value }
    fun onLastNameChange(value: String) { _lastName.value = value }
    fun onEmailChange(value: String) { _email.value = value }
    fun onPasswordChange(value: String) { _password.value = value }

    fun register() {
        viewModelScope.launch {
            _isLoading.value = true
            clearError()

            authRepository.register(
                RegisterUserRequest(
                    jmbg = _jmbg.value,
                    firstName = _firstName.value,
                    lastName = _lastName.value,
                    email = _email.value,
                    password = _password.value
                )
            )
                .onSuccess { user -> _registeredUser.value = user }
                .onError { error -> _error.value = error.toRegisterMessage() }

            _isLoading.value = false
        }
    }

    fun clearError() { _error.value = null }
}