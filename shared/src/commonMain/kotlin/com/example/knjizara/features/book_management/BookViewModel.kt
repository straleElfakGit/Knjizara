package com.example.knjizara.features.book_management

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.example.knjizara.features.book_management.dto.BookDto
import com.example.knjizara.networking.network_utils.NetworkError
import com.example.knjizara.networking.network_utils.onError
import com.example.knjizara.networking.network_utils.onSuccess
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BookViewModel(
    private val bookRepository: BookRepository
): ViewModel() {
    private val _bookList: MutableStateFlow<List<BookDto>> = MutableStateFlow(listOf())
    val bookList: StateFlow<List<BookDto>> = _bookList.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isBookBuyLoading = MutableStateFlow(false)
    val isBookBuyLoading: StateFlow<Boolean> = _isBookBuyLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _bookTitle = MutableStateFlow("")
    val bookTitle: StateFlow<String> = _bookTitle.asStateFlow()

    private val _buyBookMessage = MutableStateFlow<String?>(null)
    val buyBookMessage: StateFlow<String?> = _buyBookMessage.asStateFlow()

    fun onTitleChange(value: String) { _bookTitle.value = value }

    fun searchByTitle(title: String) {

        if (title.isBlank()) {
            _error.value = "Unesite naziv knjige"
            return
        }

        viewModelScope.launch {

            _isLoading.value = true
            clearError()

            bookRepository.searchByTitle(title)
                .onSuccess {
                    books -> _bookList.value = books
                    Logger.d("Broj knjiga: ${_bookList.value.size}")
                }
                .onError { error -> _error.value = error.toSearchBookByTitleMessage() }

            _isLoading.value = false

        }
    }

    fun byBook(isbn: String) {
        if(isbn.isBlank()) {
            _error.value = "Unesite naziv knjige"
            return
        }

        viewModelScope.launch {

            _isBookBuyLoading.value = true
            clearError()

            bookRepository.buyBook(isbn)
                .onSuccess {
                    _bookList.value = _bookList.value.map { book ->
                        if (book.isbn == isbn) {
                            book.copy(availableCopies = (book.availableCopies - 1).coerceAtLeast(0))
                        } else {
                            book
                        }
                    }
                    _buyBookMessage.value = "Knjiga je uspešno kupljena!"
                }
                .onError { error ->
                    _error.value = error.toByBookMessage()
                    _buyBookMessage.value = "Nije uspela kupovina knjige."}

            _isBookBuyLoading.value = false
        }
    }

    fun clearError() { _error.value = null }
}

private fun NetworkError.toSearchBookByTitleMessage(): String = when (this) {
    is NetworkError.HttpError -> "Greška, pokušajte ponovo, kod: ${this.code}"
    NetworkError.NoInternet -> "Proverite internet konekciju"
    NetworkError.Serialization -> "Greška u odgovoru servera"
    NetworkError.Unknown -> "Nepoznata greška, pokušajte ponovo"
}

private fun NetworkError.toByBookMessage(): String = when(this) {
    is NetworkError.HttpError -> when (code) {
        402 -> "Knjige koje koštaju više od 1000 DIN ne mogu da se kupe."
        403 -> "Niste autorizovani da obavite ovu aktivnost"
        404 -> "Ne postoji knjiga sa ovim ISBN brojem"
        409 -> "Ova knjiga nije više dostupna"
        502 -> "Problem sa bankom"
        else -> "Greška, pokušajte ponovo, kod: ${this.code}"
    }
    NetworkError.NoInternet -> "Proverite internet konekciju"
    NetworkError.Serialization -> "Greška u odgovoru servera"
    NetworkError.Unknown -> "Nepoznata greška, pokušajte ponovo"
}