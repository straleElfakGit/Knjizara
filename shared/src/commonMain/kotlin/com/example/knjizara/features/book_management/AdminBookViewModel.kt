package com.example.knjizara.features.book_management

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AdminBookViewModel(
    private val bookRepository: BookRepository
): ViewModel() {
    private val _bookTitleAdd: MutableStateFlow<String> = MutableStateFlow("")
    val bookTitleAdd: StateFlow<String> = _bookTitleAdd.asStateFlow()

    private val _authorAdd: MutableStateFlow<String> = MutableStateFlow("")
    val authorAdd: StateFlow<String> = _authorAdd.asStateFlow()
}