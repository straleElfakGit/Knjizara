package com.example.knjizara.features.auth.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDto (
    val id: Long,
    val firstName: String,
    val lastName: String,
    val role: UserRole,
    val email: String
)

@Serializable
enum class UserRole {
    USER, ADMIN
}