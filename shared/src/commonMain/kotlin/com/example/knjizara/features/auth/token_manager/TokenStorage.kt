package com.example.knjizara.features.auth.token_manager

import com.example.knjizara.features.auth.dto.UserDto

interface TokenStorage {
    suspend fun saveSession(token: String, expiresAt: Long, user: UserDto)
    suspend fun getToken(): String?
    suspend fun getUser(): UserDto?
    suspend fun isSessionValid(): Boolean
    suspend fun clearSession()
}