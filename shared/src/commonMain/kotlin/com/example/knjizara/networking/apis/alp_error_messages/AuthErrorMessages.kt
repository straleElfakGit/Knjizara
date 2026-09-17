package com.example.knjizara.networking.apis.alp_error_messages

import com.example.knjizara.networking.network_utils.NetworkError

fun NetworkError.toLoginMessage(): String = toErrorMessage { http ->
    when (http.code) {
        400 -> "Neispravni podaci"
        401 -> "Pogrešan email ili lozinka"
        429 -> "Previše puta ste probali da se prijavite sa ovog mail-a. Sačekajte 15 min"
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