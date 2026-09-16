package com.example.knjizara.features.auth.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginUserRequest (
    val email: String,
    val password: String
)