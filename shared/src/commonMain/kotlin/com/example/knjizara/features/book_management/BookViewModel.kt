package com.example.knjizara.features.book_management

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.example.knjizara.features.book_management.dto.BookWithDescriptionDto
import com.example.knjizara.networking.apis.alp_error_messages.toByBookMessage
import com.example.knjizara.networking.apis.alp_error_messages.toSearchBookByTitleMessage
import com.example.knjizara.networking.network_utils.onError
import com.example.knjizara.networking.network_utils.onSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BookViewModel(
    private val bookRepository: BookRepository
): ViewModel() {
    private val _bookList: MutableStateFlow<List<BookWithDescriptionDto>> =
        MutableStateFlow(listOf())
    val bookList: StateFlow<List<BookWithDescriptionDto>> = _bookList.asStateFlow()

    private val _selectedBook = MutableStateFlow<BookWithDescriptionDto?>(null)
    val selectedBook: StateFlow<BookWithDescriptionDto?> = _selectedBook.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isDetailsLoading = MutableStateFlow(false)
    val isDetailsLoading: StateFlow<Boolean> = _isDetailsLoading.asStateFlow()

    private val _isBookBuyLoading = MutableStateFlow(false)
    val isBookBuyLoading: StateFlow<Boolean> = _isBookBuyLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _bookTitle = MutableStateFlow("")
    val bookTitle: StateFlow<String> = _bookTitle.asStateFlow()

    private val _buyBookMessage = MutableStateFlow<String?>(null)
    val buyBookMessage: StateFlow<String?> = _buyBookMessage.asStateFlow()

    fun onTitleChange(value: String) {
        _bookTitle.value = value
    }

    fun searchByTitle(title: String) {

        if (title.isBlank()) {
            _error.value = "Unesite naziv knjige"
            return
        }

        viewModelScope.launch {

            _isLoading.value = true
            clearError()

            bookRepository.searchByTitle(title)
                .onSuccess { books ->
                    _bookList.value = books.map { BookWithDescriptionDto(book = it) }
                    Logger.d("Broj knjiga: ${_bookList.value.size}")
                }
                .onError { error -> _error.value = error.toSearchBookByTitleMessage() }

            _isLoading.value = false

        }
    }

    fun byBook(isbn: String) {
        if (isbn.isBlank()) {
            _error.value = "Unesite naziv knjige"
            return
        }

        viewModelScope.launch {

            _isBookBuyLoading.value = true
            clearError()

            bookRepository.buyBook(isbn)
                .onSuccess {
                    _bookList.value = _bookList.value.map { item ->
                        if (item.book.isbn == isbn) {
                            val updatedBook = item.book.copy(
                                availableCopies = (item.book.availableCopies - 1).coerceAtLeast(0)
                            )
                            item.copy(book = updatedBook)
                        } else {
                            item
                        }
                    }

                    _selectedBook.value?.let { current ->
                        if (current.book.isbn == isbn) {
                            val updatedBook = current.book.copy(
                                availableCopies = (current.book.availableCopies - 1).coerceAtLeast(0)
                            )
                            _selectedBook.value = current.copy(book = updatedBook)
                        }
                    }

                    _buyBookMessage.value = "Knjiga je uspešno kupljena!"
                }
                .onError { error ->
                    _error.value = error.toByBookMessage()
                    _buyBookMessage.value = "Nije uspela kupovina knjige."
                }

            _isBookBuyLoading.value = false
        }
    }

    fun loadBookDetails(bookItem: BookWithDescriptionDto) {
        _selectedBook.value = bookItem

        if (bookItem.description != null) {
            return
        }

        viewModelScope.launch {
            _isDetailsLoading.value = true

            bookRepository.findById(bookItem.book.id)
                .onSuccess { response ->
                    val updatedBookWithDesc = bookItem.copy(description = response.description)
                    _selectedBook.value = updatedBookWithDesc
                    _bookList.value = _bookList.value.map { item ->
                        if (item.book.id == bookItem.book.id) updatedBookWithDesc else item
                    }
                }
                .onError { error -> _error.value = error.toByBookMessage() }

            _isDetailsLoading.value = false
        }
    }

    fun clearSelectedBook() {
        _selectedBook.value = null
    }

    fun clearError() {
        _error.value = null
    }
}