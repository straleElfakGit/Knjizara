package com.example.knjizara.networking.apis

import com.example.knjizara.features.book_management.dto.BookDto
import com.example.knjizara.features.book_management.dto.CreateBookRequest
import com.example.knjizara.features.book_management.dto.DescriptionResponse
import com.example.knjizara.features.book_management.dto.PageResponse
import com.example.knjizara.features.book_management.dto.UpdateBookRequest
import com.example.knjizara.features.order_management.dto.OrderDto
import com.example.knjizara.networking.network_utils.EmptyResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import com.example.knjizara.networking.network_utils.NetworkError
import com.example.knjizara.networking.network_utils.Result

class BookApi(private val client: HttpClient) {
    suspend fun findBooks(page: Int = 0, size: Int = 20, sort: String = "title,asc")
    : Result<PageResponse<BookDto>, NetworkError> = safeApiCall {
        client.get("api/books") {
            parameter("page", page)
            parameter("size", size)
            parameter("sort", sort)
        }.body()
    }

    suspend fun findById(id: Long) : Result<DescriptionResponse, NetworkError> = safeApiCall {
        client.get("api/books/$id").body()
    }

    suspend fun searchByTitle(title: String): Result<List<BookDto>, NetworkError> = safeApiCall {
        client.get("api/books/search") {
            parameter("title", title)
        }.body()
    }

    suspend fun buyBook(isbn: String) : Result<OrderDto, NetworkError> = safeApiCall {
        client.post("api/books/$isbn/buy").body()
    }


    suspend fun create(request: CreateBookRequest) : Result<BookDto, NetworkError> = safeApiCall {
        client.post("api/books/admin") {
            setBody(request)
        }.body()
    }

    suspend fun update(id: Long, request: UpdateBookRequest): Result<BookDto, NetworkError> = safeApiCall {
        client.put("api/books/admin/$id") {
            setBody(request)
        }.body()
    }

    suspend fun delete(id: Long): EmptyResult<NetworkError> = safeApiCall {
        client.delete("api/books/admin/$id")
    }
}