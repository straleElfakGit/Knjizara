package com.example.knjizara.features.book_management.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import com.example.knjizara.features.book_management.dto.BookDto

@Composable
fun BooksList(
    books: List<BookDto>,
    isBuyBookLoading: Boolean,
    buyBookMessage: String?,
    onByBook: (String) -> Unit = { }
) {
    var selectedBook by remember { mutableStateOf<BookDto?>(null) }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(books) { book ->
            BookCard(book = book, onClick = { selectedBook = book })
        }
    }

    selectedBook?.let { book ->
        BookDetailsDialog(
            book = book,
            onDismiss = { selectedBook = null },
            onBuyClick = { onByBook(book.isbn) },
            isBuying = isBuyBookLoading,
            buyBookMessage = buyBookMessage
        )
    }
}