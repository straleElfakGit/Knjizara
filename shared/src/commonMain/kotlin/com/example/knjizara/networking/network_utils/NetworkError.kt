package com.example.knjizara.networking.network_utils

sealed interface NetworkError : Error {
    data object NoInternet : NetworkError
    data object Serialization : NetworkError
    data object Unknown : NetworkError

    data class HttpError(
        val code: Int,
        val serverMessage: String? = null,
        val errorCode: String? = null,
        val errors: Map<String, String>? = null
    ) : NetworkError
}