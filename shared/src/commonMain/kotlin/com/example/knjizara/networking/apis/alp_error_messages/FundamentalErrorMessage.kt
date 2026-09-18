package com.example.knjizara.networking.apis.alp_error_messages

import co.touchlab.kermit.Logger
import com.example.knjizara.networking.network_utils.NetworkError

internal fun NetworkError.toErrorMessage(
    httpHandler: (NetworkError.HttpError) -> String
): String = when (this) {
    is NetworkError.HttpError -> {
        val baseMessage = httpHandler(this)
        if(this.errors.isNullOrEmpty())
            Logger.d("Jaooo!!")
        if (!this.errors.isNullOrEmpty()) {

            val detailedErrors = this.errors.values.joinToString(
                separator = "\n• ",
                prefix = "\n• "
            )
            "$baseMessage:$detailedErrors"
        } else if (!this.serverMessage.isNullOrBlank() && baseMessage.contains("Greška")) {
            "${baseMessage}: ${this.serverMessage}"
        } else {
            baseMessage
        }
    }
    NetworkError.NoInternet -> "Proverite internet konekciju"
    NetworkError.Serialization -> "Greška u odgovoru servera"
    NetworkError.Unknown -> "Nepoznata greška, pokušajte ponovo"
}