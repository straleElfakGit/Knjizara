package com.example.knjizara.networking.apis.alp_error_messages

import com.example.knjizara.networking.network_utils.NetworkError

fun NetworkError.toLoginMessage(): String = toErrorMessage { http ->
    when (http.code) {
        400 -> "Neispravni podaci"
        401 -> "Pogrešan email ili lozinka"
        else -> "Greška, pokušajte ponovo, kod: ${http.code}"
    }
}

fun NetworkError.toRegisterMessage(): String = toErrorMessage { http ->
    when (http.code) {
        400 -> "Neispravni podaci, proverite unos"
        409 -> "Email ili JMBG već postoje u sistemu"
        else -> "Greška, pokušajte ponovo"
    }
}