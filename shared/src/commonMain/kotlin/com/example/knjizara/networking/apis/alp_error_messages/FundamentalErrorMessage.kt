package com.example.knjizara.networking.apis.alp_error_messages

import com.example.knjizara.networking.network_utils.NetworkError

internal fun NetworkError.toErrorMessage(
    httpHandler: (NetworkError.HttpError) -> String
): String = when (this) {
    is NetworkError.HttpError -> httpHandler(this)
    NetworkError.NoInternet -> "Proverite internet konekciju"
    NetworkError.Serialization -> "Greška u odgovoru servera"
    NetworkError.Unknown -> "Nepoznata greška, pokušajte ponovo"
}