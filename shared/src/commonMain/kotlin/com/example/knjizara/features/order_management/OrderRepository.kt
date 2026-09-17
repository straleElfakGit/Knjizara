package com.example.knjizara.features.order_management

import com.example.knjizara.features.order_management.dto.OrderDto
import com.example.knjizara.networking.apis.OrderApi
import com.example.knjizara.networking.network_utils.NetworkError
import com.example.knjizara.networking.network_utils.Result

class OrderRepository(
    private val orderApi: OrderApi
) {
    suspend fun findAll(): Result<List<OrderDto>, NetworkError> =
        orderApi.findAll()

    suspend fun findByUserId(id: Long): Result<List<OrderDto>, NetworkError> =
        orderApi.findByUserId(id)

    suspend fun findByBookId(id: Long): Result<List<OrderDto>, NetworkError> =
        orderApi.findByBookId(id)

    suspend fun findMyOrders(): Result<List<OrderDto>, NetworkError> =
        orderApi.findMyOrders()
}