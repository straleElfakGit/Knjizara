package com.example.knjizara.features.book_management.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.knjizara.features.auth.ui.CustomTextField
import com.example.knjizara.features.book_management.BookViewModel

@Composable
fun BookScreen(
    bookViewModel: BookViewModel,
    isAdmin: Boolean
) {
    val bookList by bookViewModel.bookList.collectAsState()
    val selectedBook by bookViewModel.selectedBook.collectAsState()
    val bookTitle by bookViewModel.bookTitle.collectAsState()
    val isLoading by bookViewModel.isLoading.collectAsState()
    val error by bookViewModel.error.collectAsState()
    val isByBookLoading by bookViewModel.isBookBuyLoading.collectAsState()
    val buyBookMessage by bookViewModel.buyBookMessage.collectAsState()
    val isDetailsLoading by bookViewModel.isDetailsLoading.collectAsState()

    Column (
        modifier = Modifier
            .fillMaxSize()
    ) {

        CustomTextField(
            value = bookTitle,
            onValueChange = { bookViewModel.onTitleChange(it) },
            label = "Naziv knjige"
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                bookViewModel.searchByTitle(title = bookTitle)
            }
        ) {
            Text("Pretraži")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                bookViewModel.findBooks()
            }
        ) {
            Text("Pretraži sve")
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            if(isLoading) {
                Box(modifier = Modifier
                    .fillMaxSize(),
                    contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                if (error != null) {
                    Text(
                        text = error!!,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
                BooksList(
                    books = bookList,
                    selectedBook = selectedBook,
                    isDetailsLoading = isDetailsLoading,
                    isBuyBookLoading = isByBookLoading,
                    onBookClick = { bookViewModel.loadBookDetails(it) },
                    onDismissDialog = {
                        bookViewModel.clearBuyBookMessage()
                        bookViewModel.clearSelectedBook() },
                    onByBook = { isbn -> bookViewModel.byBook(isbn) },
                    buyBookMessage = buyBookMessage,
                    isAdmin = isAdmin
                )
            }
        }
    }
}