package com.example.knjizara.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.example.knjizara.features.auth.AuthRepository
import com.example.knjizara.features.book_management.BookStore
import com.example.knjizara.features.order_management.OrderStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface StartupState {
    data object Loading : StartupState
    data object Authenticated : StartupState
    data object Unauthenticated : StartupState
}

class StartupViewModel(
    private val authRepository: AuthRepository,
    private val bookStore: BookStore,
    private val orderStore: OrderStore
) : ViewModel() {
    private val _state = MutableStateFlow<StartupState>(StartupState.Loading)
    val state: StateFlow<StartupState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val isValid = authRepository.restoreSession()
            if(isValid)
                Logger.d { "Korisnik je prijavljen!" }
            else
                Logger.d { "Korisnik nije prijavljen." }
            _state.value = if (isValid) StartupState.Authenticated else StartupState.Unauthenticated
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _state.value = StartupState.Unauthenticated
            bookStore.clear()
            orderStore.clear()
        }
    }
}