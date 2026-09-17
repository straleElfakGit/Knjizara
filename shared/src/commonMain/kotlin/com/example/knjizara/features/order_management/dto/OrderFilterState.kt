package com.example.knjizara.features.order_management.dto

data class OrderFilterState(
    val filterByBookId: Boolean = false,
    val bookId: String = "",
    val filterByUserId: Boolean = false,
    val userId: String = ""
)
