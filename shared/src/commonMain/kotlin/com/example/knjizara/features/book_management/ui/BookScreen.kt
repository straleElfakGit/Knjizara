package com.example.knjizara.features.book_management.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.knjizara.features.book_management.AdminBookViewModel
import com.example.knjizara.features.book_management.BookViewModel
import com.example.knjizara.features.book_management.ui.admin.AdminBookDialogs
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import kotlin.time.Duration.Companion.milliseconds
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.text.input.ImeAction

@OptIn(KoinExperimentalAPI::class)
@Composable
fun BookScreen(
    bookViewModel: BookViewModel,
    adminBookViewModel: AdminBookViewModel = koinViewModel<AdminBookViewModel>(),
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

    val successMessage by adminBookViewModel.successMessage.collectAsState()

    LaunchedEffect(successMessage) {
        if (successMessage != null) {
            delay(2500.milliseconds)
            adminBookViewModel.clearSuccessMessage()
        }
    }

    Scaffold(
        floatingActionButton = {
            if (isAdmin) {
                FloatingActionButton(
                    onClick = { adminBookViewModel.showActionMenu() }
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Admin akcije")
                }
            }
        }
    ) {  innerPadding ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = bookTitle,
                    onValueChange = { bookViewModel.onTitleChange(it) },
                    label = { Text("Naziv knjige") },
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        if (bookTitle.isNotEmpty()) {
                            IconButton(onClick = { bookViewModel.onTitleChange("") }) {
                                Icon(Icons.Default.Clear, contentDescription = "Obriši")
                            }
                        }
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = { bookViewModel.searchByTitle(bookTitle) }
                    ),
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = { bookViewModel.findBooks() }) {
                    Icon(Icons.Default.List, contentDescription = "Prikaži sve knjige")
                }
            }

            if (successMessage != null) {
                Text(
                    text = successMessage!!,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

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
                        buyBookMessage = buyBookMessage
                    )
                }
            }
        }
    }
    AdminBookDialogs(viewModel = adminBookViewModel)
}