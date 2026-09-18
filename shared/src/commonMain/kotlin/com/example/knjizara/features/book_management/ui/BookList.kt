package com.example.knjizara.features.book_management.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import com.example.knjizara.features.book_management.dto.BookWithDescriptionDto

@Composable
fun BooksList(
    books: List<BookWithDescriptionDto>,
    selectedBook: BookWithDescriptionDto?,
    isDetailsLoading: Boolean,
    isBuyBookLoading: Boolean,
    buyBookMessage: String?,
    onBookClick: (BookWithDescriptionDto) -> Unit,
    onDismissDialog: () -> Unit,
    onByBook: (String) -> Unit = { }
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(books) { item ->
            BookCard(
                book = item.book,
                onClick = { onBookClick(item) })
        }
    }

    selectedBook?.let { bookItem ->
        BookDetailsDialog(
            bookItem = bookItem,
            onDismiss = onDismissDialog,
            isLoadingDescription = isDetailsLoading,
            onBuyClick = { onByBook(bookItem.book.isbn) },
            isBuying = isBuyBookLoading,
            buyBookMessage = buyBookMessage,
        )
    }
}