package com.example.knjizara.features.book_management

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.example.knjizara.features.book_management.dto.BookWithDescriptionDto
import com.example.knjizara.features.order_management.OrderStore
import com.example.knjizara.networking.apis.alp_error_messages.toByBookMessage
import com.example.knjizara.networking.apis.alp_error_messages.toFindBooksMessage
import com.example.knjizara.networking.apis.alp_error_messages.toFindByIdMessage
import com.example.knjizara.networking.apis.alp_error_messages.toSearchBookByTitleMessage
import com.example.knjizara.networking.network_utils.onError
import com.example.knjizara.networking.network_utils.onSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BookViewModel(
    private val bookRepository: BookRepository,
    private val bookStore: BookStore,
    private val orderStore: OrderStore
): ViewModel() {
    val bookList: StateFlow<List<BookWithDescriptionDto>> = bookStore.books

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
                    bookStore.setBooks(books)
                    Logger.d("Broj knjiga: ${books.size}")
                }
                .onError { error -> _error.value = error.toSearchBookByTitleMessage() }

            _isLoading.value = false

        }
    }

    fun byBook(isbn: String) {
        if (isbn.isBlank()) {
            _buyBookMessage.value = "Unesite isbn knjige"
            return
        }

        viewModelScope.launch {

            _isBookBuyLoading.value = true
            clearError()

            bookRepository.buyBook(isbn)
                .onSuccess { order->
                    orderStore.addOrder(order)
                    bookStore.decrementAvailableCopies(isbn)

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
                    _buyBookMessage.value = "Nije uspela kupovina knjige: ${error.toByBookMessage()}"
                }

            _isBookBuyLoading.value = false
        }
    }

    fun clearBuyBookMessage() {
        _buyBookMessage.value = null
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
                    bookStore.updateBookDescription(bookItem.book.id, response.description)
                }
                .onError { error -> _error.value = error.toFindByIdMessage() }

            _isDetailsLoading.value = false
        }
    }

    fun findBooks(page: Int = 0,
                  size: Int = 20,
                  sort: String = "title,asc") {

        viewModelScope.launch {

            _isLoading.value = true
            clearError()

            bookRepository.findBooks(page, size, sort)
                .onSuccess { response ->
                    bookStore.setBooks(response.content)
                    Logger.d("Broj knjiga: ${response.content.size}")
                }
                .onError { error -> _error.value = error.toFindBooksMessage() }

            _isLoading.value = false
        }
    }

    fun clearSelectedBook() {
        _selectedBook.value = null
    }

    fun clearError() {
        _error.value = null
    }
}