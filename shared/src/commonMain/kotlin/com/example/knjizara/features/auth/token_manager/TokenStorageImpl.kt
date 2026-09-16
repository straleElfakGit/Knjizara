package com.example.knjizara.features.auth.token_manager

import co.touchlab.kermit.Logger
import com.russhwolf.settings.Settings
import com.example.knjizara.features.auth.dto.UserDto
import kotlinx.serialization.json.Json
import kotlin.time.Clock

class TokenStorageImpl(
    private val settings: Settings)
    : TokenStorage {

    override suspend fun saveSession(token: String, expiresAt: Long, user: UserDto) {
        settings.putString(KEY_TOKEN, token)
        settings.putLong(KEY_EXPIRES_AT, expiresAt)
        settings.putString(KEY_USER, Json.encodeToString(user))
    }

    override suspend fun getToken(): String? {
        val token = settings.getStringOrNull(KEY_TOKEN)
        Logger.d("Token je $token")
        return token
    }

    override suspend fun getUser(): UserDto? =
        settings.getStringOrNull(KEY_USER)?.let {
            runCatching {
                Json.decodeFromString<UserDto>(it)
            }.getOrNull()
        }

    override suspend fun isSessionValid(): Boolean {
        val hasToken = settings.getStringOrNull(KEY_TOKEN) != null
        val expiresAt = settings.getLongOrNull(KEY_EXPIRES_AT) ?: return false
        return hasToken && Clock.System.now().toEpochMilliseconds() < expiresAt
    }

    override suspend fun clearSession() {
        settings.remove(KEY_TOKEN)
        settings.remove(KEY_EXPIRES_AT)
        settings.remove(KEY_USER)
    }

    private companion object {
        const val KEY_TOKEN = "auth_token"
        const val KEY_EXPIRES_AT = "auth_token_expires_at"
        const val KEY_USER = "auth_user"
    }
}