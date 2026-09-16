package com.example.knjizara.networking.apis.alp_error_messages

import com.example.knjizara.networking.network_utils.NetworkError

fun NetworkError.toSearchBookByTitleMessage(): String = toErrorMessage { http ->
    "Greška, pokušajte ponovo, kod: ${http.code}"
}

fun NetworkError.toByBookMessage(): String = toErrorMessage { http ->
    when (http.code) {
        402 -> "Knjige koje koštaju više od 1000 DIN ne mogu da se kupe."
        403 -> "Niste autorizovani da obavite ovu aktivnost"
        404 -> "Ne postoji knjiga sa ovim ISBN brojem"
        409 -> "Ova knjiga nije više dostupna"
        502 -> "Problem sa bankom"
        else -> "Greška, pokušajte ponovo, kod: ${http.code}"
    }
}