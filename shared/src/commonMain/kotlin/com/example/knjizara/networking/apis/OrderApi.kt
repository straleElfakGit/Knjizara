package com.example.knjizara.networking.apis

import com.example.knjizara.features.order_management.dto.OrderDto
import com.example.knjizara.networking.network_utils.NetworkError
import io.ktor.client.HttpClient
import com.example.knjizara.networking.network_utils.Result
import io.ktor.client.call.body
import io.ktor.client.request.get

class OrderApi(private val client: HttpClient) {

    suspend fun findAll(): Result<List<OrderDto>, NetworkError> = safeApiCall {
        client.get("api/orders").body()
    }

    suspend fun findByUserId(id: Long): Result<List<OrderDto>, NetworkError> = safeApiCall {
        client.get("api/orders/user/$id").body()
    }

    suspend fun findByBookId(id: Long): Result<List<OrderDto>, NetworkError> = safeApiCall {
        client.get("api/orders/book/$id").body()
    }

    suspend fun findMyOrders(): Result<List<OrderDto>, NetworkError> = safeApiCall {
        client.get("api/orders/me").body()
    }
}