package com.example.knjizara.features.book_management.ui.admin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.knjizara.features.book_management.AdminBookAction
import com.example.knjizara.features.book_management.AdminBookViewModel

@Composable
fun AdminBookDialogs(viewModel: AdminBookViewModel) {
    val isActionMenuVisible by viewModel.isActionMenuVisible.collectAsState()
    val activeAction by viewModel.activeAction.collectAsState()
    val isSubmitting by viewModel.isSubmitting.collectAsState()
    val error by viewModel.formError.collectAsState()

    if (isActionMenuVisible) {
        AdminActionMenuDialog(
            onSelectAction = viewModel::selectAction,
            onDismiss = viewModel::hideActionMenu
        )
    }

    when (activeAction) {
        AdminBookAction.CREATE -> {
            val form by viewModel.createForm.collectAsState()
            CreateBookDialog(
                form = form,
                isSubmitting = isSubmitting,
                error = error,
                onTitleChange = viewModel::onCreateTitleChange,
                onAuthorChange = viewModel::onCreateAuthorChange,
                onIsbnChange = viewModel::onCreateIsbnChange,
                onPublishedYearChange = viewModel::onCreatePublishedYearChange,
                onAvailableCopiesChange = viewModel::onCreateAvailableCopiesChange,
                onPriceChange = viewModel::onCreatePriceChange,
                onDescriptionChange = viewModel::onDescriptionChange,
                onSubmit = viewModel::submitCreate,
                onDismiss = viewModel::dismissForm
            )
        }
        AdminBookAction.UPDATE -> {
            val form by viewModel.updateForm.collectAsState()
            UpdateBookDialog(
                form = form,
                isSubmitting = isSubmitting,
                error = error,
                onIdChange = viewModel::onUpdateIdChange,
                onTitleChange = viewModel::onUpdateTitleChange,
                onAuthorChange = viewModel::onUpdateAuthorChange,
                onPublishedYearChange = viewModel::onUpdatePublishedYearChange,
                onAvailableCopiesChange = viewModel::onUpdateAvailableCopiesChange,
                onPriceChange = viewModel::onUpdatePriceChange,
                onSubmit = viewModel::submitUpdate,
                onDismiss = viewModel::dismissForm
            )
        }
        AdminBookAction.DELETE -> {
            val form by viewModel.deleteForm.collectAsState()
            DeleteBookDialog(
                form = form,
                isSubmitting = isSubmitting,
                error = error,
                onIdChange = viewModel::onDeleteIdChange,
                onSubmit = viewModel::submitDelete,
                onDismiss = viewModel::dismissForm
            )
        }
        null -> Unit
    }
}