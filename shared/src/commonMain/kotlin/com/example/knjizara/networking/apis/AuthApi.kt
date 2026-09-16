package com.example.knjizara.networking.apis

import com.example.knjizara.features.auth.dto.AuthResponse
import com.example.knjizara.features.auth.dto.LoginUserRequest
import com.example.knjizara.features.auth.dto.RegisterUserRequest
import com.example.knjizara.features.auth.dto.UserDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.setBody
import io.ktor.client.request.post
import com.example.knjizara.networking.network_utils.NetworkError
import com.example.knjizara.networking.network_utils.Result

class AuthApi(private val client: HttpClient) {
    suspend fun register(request: RegisterUserRequest): Result<UserDto, NetworkError> = safeApiCall {
        client.post("api/auth/register") {
            setBody(request)
        }.body()
    }

    suspend fun login(request: LoginUserRequest): Result<AuthResponse, NetworkError> = safeApiCall {
        client.post("api/auth/login") {
            setBody(request)
        }.body()
    }
}