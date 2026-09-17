package com.example.knjizara.networking.apis.alp_error_messages

import com.example.knjizara.networking.network_utils.NetworkError

fun NetworkError.toFindMyOrdersMessage(): String = toErrorMessage { http ->
    when (http.code) {
        401 -> "Sesija je istekla, probajte ponovo"
        403 -> "Niste autorizovani da obavite ovu aktivnost"
        else -> "Greška, pokušajte ponovo, kod: ${http.code}"
    }
}

fun NetworkError.toFindAllOrdersMessage(): String = toErrorMessage { http ->
    when(http.code) {
        403 -> "Korisnik mora da bude admin da bi obavio ovu aktivnost"
        else -> "Greška, pokušajte ponovo, kod: ${http.code}"
    }
}

fun NetworkError.toFindOrdersByUserIdMessage(): String = toErrorMessage { http ->
    when(http.code) {
        403 -> "Korisnik mora da bude admin da bi obavio ovu aktivnost"
        else -> "Greška, pokušajte ponovo, kod: ${http.code}"
    }
}

fun NetworkError.toFindOrdersByBookIdMessage(): String = toErrorMessage { http ->
    when(http.code) {
        403 -> "Korisnik mora da bude admin da bi obavio ovu aktivnost"
        else -> "Greška, pokušajte ponovo, kod: ${http.code}"
    }
}

fun NetworkError.toFindOrdersMessage(): String = toErrorMessage { http ->
    when(http.code) {
        403 -> "Korisnik mora da bude admin da bi obavio ovu aktivnost"
        else -> "Greška, pokušajte ponovo, kod: ${http.code}"
    }
}