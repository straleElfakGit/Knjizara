package com.example.knjizara.features.auth.dto

import kotlinx.serialization.Serializable

@Serializable
data class RegisterUserRequest(
    val jmbg: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String
)
