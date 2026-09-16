package com.example.knjizara.features.auth

import co.touchlab.kermit.Logger
import com.example.knjizara.features.auth.dto.LoginUserRequest
import com.example.knjizara.features.auth.dto.RegisterUserRequest
import com.example.knjizara.features.auth.dto.UserDto
import com.example.knjizara.features.auth.token_manager.TokenStorage
import com.example.knjizara.networking.apis.AuthApi
import kotlin.time.Clock
import kotlin.time.Duration.Companion.hours
import com.example.knjizara.networking.network_utils.NetworkError
import com.example.knjizara.networking.network_utils.Result
import com.example.knjizara.networking.network_utils.onSuccess
import com.example.knjizara.networking.network_utils.map

class AuthRepository(
    private val authApi: AuthApi,
    private val tokenStorage: TokenStorage,
    private val sessionManager: SessionManager
) {
    suspend fun register(request: RegisterUserRequest): Result<UserDto, NetworkError> {
        val registerResult = authApi.register(request)
        if (registerResult !is Result.Success)
            return registerResult

        val loginResult = login(LoginUserRequest(request.email, request.password))
        return loginResult.map { registerResult.data }
    }

    suspend fun login(request: LoginUserRequest): Result<UserDto, NetworkError> {
        return authApi.login(request)
            .onSuccess { response ->
                val expiresAt = (Clock.System.now() + 24.hours).toEpochMilliseconds()
                tokenStorage.saveSession(response.token, expiresAt, response.user)
                sessionManager.onLoginSuccess(response.user)
            }
            .map { it.user }
    }

    suspend fun logout() {
        tokenStorage.clearSession()
        sessionManager.logout()
    }

    suspend fun restoreSession(): Boolean {
        val isValid = tokenStorage.isSessionValid()
        if (isValid) {
            tokenStorage.getUser()?.let {
                sessionManager.onLoginSuccess(it)
                Logger.d("Ovo je korisnik: $it")
            }
        } else {
            tokenStorage.clearSession()
        }
        return isValid
    }
}