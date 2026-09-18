package com.example.knjizara.features.book_management.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.knjizara.features.auth.ui.CustomTextField
import com.example.knjizara.features.book_management.state.CreateBookFormState

@Composable
fun CreateBookDialog(
    form: CreateBookFormState,
    isSubmitting: Boolean,
    error: String?,
    onTitleChange: (String) -> Unit,
    onAuthorChange: (String) -> Unit,
    onIsbnChange: (String) -> Unit,
    onPublishedYearChange: (String) -> Unit,
    onAvailableCopiesChange: (String) -> Unit,
    onPriceChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Dodaj knjigu",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                CustomTextField(value = form.title, onValueChange = onTitleChange, label = "Naziv")
                Spacer(modifier = Modifier.height(12.dp))

                CustomTextField(value = form.author, onValueChange = onAuthorChange, label = "Autor")
                Spacer(modifier = Modifier.height(12.dp))

                CustomTextField(
                    value = form.isbn,
                    onValueChange = onIsbnChange,
                    label = "ISBN",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(modifier = Modifier.height(12.dp))

                CustomTextField(
                    value = form.publishedYear,
                    onValueChange = onPublishedYearChange,
                    label = "Godina izdanja",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(modifier = Modifier.height(12.dp))

                CustomTextField(
                    value = form.availableCopies,
                    onValueChange = onAvailableCopiesChange,
                    label = "Broj primeraka",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(modifier = Modifier.height(12.dp))

                CustomTextField(
                    value = form.price,
                    onValueChange = onPriceChange,
                    label = "Cena",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
                Spacer(modifier = Modifier.height(12.dp))

                CustomTextField(value = form.description, onValueChange = onDescriptionChange, label = "Opis")

                if (error != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = error, color = MaterialTheme.colorScheme.error)
                }

                Spacer(modifier = Modifier.height(24.dp))

                androidx.compose.foundation.layout.Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f)) {
                        Text("Otkaži")
                    }
                    Button(onClick = onSubmit, enabled = !isSubmitting, modifier = Modifier.weight(1f)) {
                        if (isSubmitting) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text("Sačuvaj")
                        }
                    }
                }
            }
        }
    }
}