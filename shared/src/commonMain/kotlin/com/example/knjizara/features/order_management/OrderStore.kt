package com.example.knjizara.features.order_management

import com.example.knjizara.features.book_management.dto.BookDto
import com.example.knjizara.features.order_management.dto.OrderDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class OrderStore {
    private val _myOrders = MutableStateFlow<List<OrderDto>>(emptyList())
    val myOrders: StateFlow<List<OrderDto>> = _myOrders.asStateFlow()

    fun setOrders(orders: List<OrderDto>) {
        _myOrders.value = orders
    }

    fun addOrder(order: OrderDto) {
        _myOrders.update { current -> listOf(order) + current }
    }

    fun updateBook(updatedBook: BookDto) {
        _myOrders.update { currentOrders ->
            currentOrders.map { order ->
                if (order.book.id == updatedBook.id) {
                    order.copy(book = updatedBook)
                } else {
                    order
                }
            }
        }
    }

    fun clear() {
        _myOrders.value = emptyList()
    }
}