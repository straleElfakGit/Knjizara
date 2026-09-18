package com.example.knjizara.features.book_management.state

data class CreateBookFormState(
    val title: String = "",
    val author: String = "",
    val isbn: String = "",
    val publishedYear: String = "",
    val availableCopies: String = "",
    val price: String = "",
    val description: String = ""
)
