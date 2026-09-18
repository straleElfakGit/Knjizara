package com.example.knjizara.features.book_management

import com.example.knjizara.features.book_management.dto.BookDto
import com.example.knjizara.features.book_management.dto.BookWithDescriptionDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BookStore {
    private val _books = MutableStateFlow<List<BookWithDescriptionDto>>(emptyList())
    val books: StateFlow<List<BookWithDescriptionDto>> = _books.asStateFlow()

    fun setBooks(books: List<BookDto>) {
        _books.value = books.map { BookWithDescriptionDto(book = it) }
    }

    fun addBook(book: BookDto) {
        _books.update { current -> listOf(BookWithDescriptionDto(book = book)) + current }
    }

    fun updateBook(book: BookDto) {
        _books.update { current ->
            current.map { item -> if (item.book.id == book.id) item.copy(book = book) else item }
        }
    }

    fun updateBookDescription(id: Long, description: String?) {
        _books.update { current ->
            current.map { item -> if (item.book.id == id) item.copy(description = description) else item }
        }
    }

    fun removeBook(id: Long) {
        _books.update { current -> current.filterNot { it.book.id == id } }
    }

    fun decrementAvailableCopies(isbn: String) {
        _books.update { current ->
            current.map { item ->
                if (item.book.isbn == isbn) {
                    item.copy(book = item.book.copy(availableCopies = (item.book.availableCopies - 1).coerceAtLeast(0)))
                } else item
            }
        }
    }

    fun clear() {
        _books.value = emptyList()
    }
}