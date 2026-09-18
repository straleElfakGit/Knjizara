package com.example.knjizara.features.book_management

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.knjizara.features.book_management.dto.CreateBookRequest
import com.example.knjizara.features.book_management.dto.UpdateBookRequest
import com.example.knjizara.features.book_management.state.CreateBookFormState
import com.example.knjizara.features.book_management.state.DeleteBookFormState
import com.example.knjizara.features.book_management.state.UpdateBookFormState
import com.example.knjizara.features.order_management.OrderStore
import com.example.knjizara.networking.apis.alp_error_messages.toCreateBookMessage
import com.example.knjizara.networking.apis.alp_error_messages.toDeleteBookMessage
import com.example.knjizara.networking.apis.alp_error_messages.toUpdateBookMessage
import com.example.knjizara.networking.network_utils.onError
import com.example.knjizara.networking.network_utils.onSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class AdminBookAction { CREATE, UPDATE, DELETE }

class AdminBookViewModel(
    private val bookRepository: BookRepository,
    private val bookStore: BookStore,
    private val orderStore: OrderStore
): ViewModel() {
    private val _isActionMenuVisible = MutableStateFlow(false)
    val isActionMenuVisible: StateFlow<Boolean> = _isActionMenuVisible.asStateFlow()

    private val _activeAction = MutableStateFlow<AdminBookAction?>(null)
    val activeAction: StateFlow<AdminBookAction?> = _activeAction.asStateFlow()

    private val _createForm = MutableStateFlow(CreateBookFormState())
    val createForm: StateFlow<CreateBookFormState> = _createForm.asStateFlow()

    private val _updateForm = MutableStateFlow(UpdateBookFormState())
    val updateForm: StateFlow<UpdateBookFormState> = _updateForm.asStateFlow()

    private val _deleteForm = MutableStateFlow(DeleteBookFormState())
    val deleteForm: StateFlow<DeleteBookFormState> = _deleteForm.asStateFlow()

    private val _isSubmitting = MutableStateFlow(false)
    val isSubmitting: StateFlow<Boolean> = _isSubmitting.asStateFlow()

    private val _formError = MutableStateFlow<String?>(null)
    val formError: StateFlow<String?> = _formError.asStateFlow()

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage.asStateFlow()

    fun showActionMenu() { _isActionMenuVisible.value = true }
    fun hideActionMenu() { _isActionMenuVisible.value = false }

    fun selectAction(action: AdminBookAction) {
        _isActionMenuVisible.value = false
        _formError.value = null
        _createForm.value = CreateBookFormState()
        _updateForm.value = UpdateBookFormState()
        _deleteForm.value = DeleteBookFormState()
        _activeAction.value = action
    }

    fun dismissForm() {
        _activeAction.value = null
        _formError.value = null
    }

    fun clearSuccessMessage() { _successMessage.value = null }

    // Create
    fun onCreateTitleChange(value: String) { _createForm.update { it.copy(title = value) } }
    fun onCreateAuthorChange(value: String) { _createForm.update { it.copy(author = value) } }
    fun onCreateIsbnChange(value: String) { _createForm.update { it.copy(isbn = value) } }
    fun onCreatePublishedYearChange(value: String) { _createForm.update { it.copy(publishedYear = value) } }
    fun onCreateAvailableCopiesChange(value: String) { _createForm.update { it.copy(availableCopies = value) } }
    fun onCreatePriceChange(value: String) { _createForm.update { it.copy(price = value) } }
    fun onDescriptionChange(value: String) {_createForm.update { it.copy(description = value) }}

    fun submitCreate() {
        val form = _createForm.value

        if (form.title.isBlank() || form.author.isBlank() || form.isbn.isBlank()) {
            _formError.value = "Popunite sva obavezna polja"
            return
        }

        val publishedYear = form.publishedYear.toIntOrNull()
        val availableCopies = form.availableCopies.toIntOrNull()
        val price = form.price.toDoubleOrNull()

        if (publishedYear == null || availableCopies == null || price == null) {
            _formError.value = "Godina, količina i cena moraju biti brojevi"
            return
        }

        viewModelScope.launch {
            _isSubmitting.value = true
            _formError.value = null

            bookRepository.create(
                CreateBookRequest(
                    title = form.title,
                    author = form.author,
                    isbn = form.isbn,
                    publishedYear = publishedYear,
                    availableCopies = availableCopies,
                    price = price,
                    description = form.description
                )
            )
                .onSuccess { book ->
                    bookStore.addBook(book)
                    _successMessage.value = "Knjiga je uspešno dodata"
                    _activeAction.value = null
                }
                .onError { error -> _formError.value = error.toCreateBookMessage() }

            _isSubmitting.value = false
        }
    }

    // Update
    fun onUpdateIdChange(value: String) { _updateForm.update { it.copy(id = value) } }
    fun onUpdateTitleChange(value: String) { _updateForm.update { it.copy(title = value) } }
    fun onUpdateAuthorChange(value: String) { _updateForm.update { it.copy(author = value) } }
    fun onUpdatePublishedYearChange(value: String) { _updateForm.update { it.copy(publishedYear = value) } }
    fun onUpdateAvailableCopiesChange(value: String) { _updateForm.update { it.copy(availableCopies = value) } }
    fun onUpdatePriceChange(value: String) { _updateForm.update { it.copy(price = value) } }

    fun submitUpdate() {
        val form = _updateForm.value
        val id = form.id.toLongOrNull()

        if (id == null) {
            _formError.value = "Unesite validan ID knjige"
            return
        }
        if (form.title.isBlank() || form.author.isBlank()) {
            _formError.value = "Popunite sva obavezna polja"
            return
        }

        val publishedYear = form.publishedYear.toIntOrNull()
        val availableCopies = form.availableCopies.toIntOrNull()
        val price = form.price.toDoubleOrNull()

        if (publishedYear == null || availableCopies == null || price == null) {
            _formError.value = "Godina, količina i cena moraju biti brojevi"
            return
        }

        viewModelScope.launch {
            _isSubmitting.value = true
            _formError.value = null

            bookRepository.update(
                id = id,
                request = UpdateBookRequest(
                    title = form.title,
                    author = form.author,
                    publishedYear = publishedYear,
                    availableCopies = availableCopies,
                    price = price
                )
            )
                .onSuccess { book ->
                    bookStore.updateBook(book)
                    _successMessage.value = "Knjiga je uspešno ažurirana"
                    _activeAction.value = null
                    orderStore.updateBook(book)
                }
                .onError { error -> _formError.value = error.toUpdateBookMessage() }

            _isSubmitting.value = false
        }
    }

    // Delete
    fun onDeleteIdChange(value: String) { _deleteForm.update { it.copy(id = value) } }

    fun submitDelete() {
        val id = _deleteForm.value.id.toLongOrNull()

        if (id == null) {
            _formError.value = "Unesite validan ID knjige"
            return
        }

        viewModelScope.launch {
            _isSubmitting.value = true
            _formError.value = null

            bookRepository.delete(id)
                .onSuccess {
                    bookStore.removeBook(id)
                    _successMessage.value = "Knjiga je uspešno obrisana"
                    _activeAction.value = null
                }
                .onError { error -> _formError.value = error.toDeleteBookMessage() }

            _isSubmitting.value = false
        }
    }
}