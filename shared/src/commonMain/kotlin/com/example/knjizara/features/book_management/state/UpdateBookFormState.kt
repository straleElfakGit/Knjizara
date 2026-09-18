package com.example.knjizara.features.book_management.state

data class UpdateBookFormState(
    val id: String = "",
    val title: String = "",
    val author: String = "",
    val publishedYear: String = "",
    val availableCopies: String = "",
    val price: String = ""
)
