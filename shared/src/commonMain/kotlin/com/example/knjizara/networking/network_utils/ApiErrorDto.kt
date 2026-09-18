package com.example.knjizara.networking.network_utils

import kotlinx.serialization.Serializable

@Serializable
data class ApiErrorDto(
    val title: String? = null,
    val detail: String? = null,
    val status: Int? = null,
    val instance: String? = null,
    val errors: Map<String, String>? = null
)
