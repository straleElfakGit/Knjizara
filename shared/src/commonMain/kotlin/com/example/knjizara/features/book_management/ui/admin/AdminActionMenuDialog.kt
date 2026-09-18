package com.example.knjizara.features.book_management.ui.admin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.knjizara.features.book_management.AdminBookAction

@Composable
fun AdminActionMenuDialog(
    onSelectAction: (AdminBookAction) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = "Admin akcije",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { onSelectAction(AdminBookAction.CREATE) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Dodaj knjigu")
                }
                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { onSelectAction(AdminBookAction.UPDATE) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ažuriraj knjigu")
                }
                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { onSelectAction(AdminBookAction.DELETE) },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Obriši knjigu")
                }
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                    Text("Otkaži")
                }
            }
        }
    }
}