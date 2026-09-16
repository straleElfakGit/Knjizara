package com.example.knjizara.features.book_management.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateBookRequest(
    val title: String,
    val author: String,
    val isbn: String,
    val publishedYear: Int,
    val availableCopies: Int,
    val price: Double
)