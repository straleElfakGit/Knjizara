package com.example.knjizara.features.book_management.dto

data class BookWithDescriptionDto(
    val book: BookDto,
    val description: String? = null
)
