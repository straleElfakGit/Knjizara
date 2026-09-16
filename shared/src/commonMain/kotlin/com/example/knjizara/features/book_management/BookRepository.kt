package com.example.knjizara.features.book_management

import com.example.knjizara.networking.apis.BookApi
import com.example.knjizara.features.book_management.dto.BookDto
import com.example.knjizara.features.book_management.dto.CreateBookRequest
import com.example.knjizara.features.book_management.dto.DescriptionResponse
import com.example.knjizara.features.book_management.dto.PageResponse
import com.example.knjizara.features.book_management.dto.UpdateBookRequest
import com.example.knjizara.features.order_management.dto.OrderDto
import com.example.knjizara.networking.network_utils.EmptyResult
import com.example.knjizara.networking.network_utils.NetworkError
import com.example.knjizara.networking.network_utils.Result

class BookRepository (
    private val bookApi: BookApi
) {
    suspend fun findBooks(
        page: Int = 0,
        size: Int = 20,
        sort: String = "title,asc"
    ): Result<PageResponse<BookDto>, NetworkError> = bookApi.findBooks(page, size, sort)

    suspend fun findById(id: Long): Result<DescriptionResponse, NetworkError> =
        bookApi.findById(id)

    suspend fun searchByTitle(title: String): Result<List<BookDto>, NetworkError> =
        bookApi.searchByTitle(title)

    suspend fun buyBook(isbn: String): Result<OrderDto, NetworkError> =
        bookApi.buyBook(isbn)

    suspend fun create(request: CreateBookRequest): Result<BookDto, NetworkError> =
        bookApi.create(request)

    suspend fun update(id: Long, request: UpdateBookRequest): Result<BookDto, NetworkError> =
        bookApi.update(id, request)

    suspend fun delete(id: Long): EmptyResult<NetworkError> =
        bookApi.delete(id)
}