package com.example.knjizara.features.order_management.dto

import com.example.knjizara.features.book_management.dto.BookDto
import com.example.knjizara.features.auth.dto.UserDto
import kotlinx.serialization.Serializable

@Serializable
data class OrderDto(
    val id: Long,
    val user: UserDto,
    val book: BookDto,
    val priceAtPurchase: Double,
    val createdAt: String,
    val transactionId: String
)
