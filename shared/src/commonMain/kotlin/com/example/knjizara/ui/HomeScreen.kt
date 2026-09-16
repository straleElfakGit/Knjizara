package com.example.knjizara.ui

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
import com.example.knjizara.features.auth.dto.UserDto
import com.example.knjizara.features.auth.ui.CustomTextField
import com.example.knjizara.features.book_management.BookViewModel
import com.example.knjizara.features.book_management.ui.BooksList
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun HomeScreen(
    user: UserDto,
    bookViewModel: BookViewModel = koinViewModel<BookViewModel> (),
    onLogout: () -> Unit = {}
) {
    val bookList by bookViewModel.bookList.collectAsState()
    val bookTitle by bookViewModel.bookTitle.collectAsState()
    val isLoading by bookViewModel.isLoading.collectAsState()
    val error by bookViewModel.error.collectAsState()
    val isByBookLoading by bookViewModel.isBookBuyLoading.collectAsState()
    val buyBookMessage by bookViewModel.buyBookMessage.collectAsState()

    Column (
        modifier = Modifier
            .fillMaxSize()
    ) {
        Button(
            onClick = onLogout
        ) {

            Text("Izloguj se")
        }
        Spacer(modifier = Modifier.height(16.dp) )

        CustomTextField(
            value = bookTitle,
            onValueChange = { bookViewModel.onTitleChange(it) },
            label = bookTitle
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                bookViewModel.searchByTitle(title = bookTitle)
            }
        ) {
            Text("Pretrazi")
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
                } else {
                    BooksList(bookList, isByBookLoading, buyBookMessage) { bookIsbn ->
                        bookViewModel.byBook(bookIsbn)
                    }
                }
            }
        }
    }
}