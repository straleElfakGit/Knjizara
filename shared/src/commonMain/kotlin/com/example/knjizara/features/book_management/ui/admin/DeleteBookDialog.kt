package com.example.knjizara.features.book_management.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.example.knjizara.features.book_management.state.DeleteBookFormState

@Composable
fun DeleteBookDialog(
    form: DeleteBookFormState,
    isSubmitting: Boolean,
    error: String?,
    onIdChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("Obriši knjigu", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Ova akcija je nepovratna.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))

                CustomTextField(
                    value = form.id,
                    onValueChange = onIdChange,
                    label = "ID knjige",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                if (error != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = error, color = MaterialTheme.colorScheme.error)
                }

                Spacer(modifier = Modifier.height(24.dp))
                androidx.compose.foundation.layout.Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f)) { Text("Otkaži") }
                    Button(
                        onClick = onSubmit,
                        enabled = !isSubmitting,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                        modifier = Modifier.weight(1f)
                    ) {
                        if (isSubmitting) {
                            CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp, color = MaterialTheme.colorScheme.onError)
                        } else {
                            Text("Obriši")
                        }
                    }
                }
            }
        }
    }
}