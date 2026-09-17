package com.example.knjizara.features.order_management

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.knjizara.features.order_management.dto.OrderDto
import com.example.knjizara.features.order_management.dto.OrderFilterState
import com.example.knjizara.features.order_management.dto.OrdersViewMode
import com.example.knjizara.networking.apis.alp_error_messages.toFindMyOrdersMessage
import com.example.knjizara.networking.apis.alp_error_messages.toFindOrdersMessage
import com.example.knjizara.networking.network_utils.onError
import com.example.knjizara.networking.network_utils.onSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OrderViewModel(
    private val orderRepository: OrderRepository,
    private val orderStore: OrderStore
): ViewModel() {

    private val _viewMode = MutableStateFlow(OrdersViewMode.MY_ORDERS)
    val viewMode: StateFlow<OrdersViewMode> = _viewMode.asStateFlow()

    val orders: StateFlow<List<OrderDto>> = orderStore.myOrders

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _filteredOrders = MutableStateFlow<List<OrderDto>>(emptyList())
    val filteredOrders: StateFlow<List<OrderDto>> = _filteredOrders.asStateFlow()

    private val _isFilterLoading = MutableStateFlow(false)
    val isFilterLoading: StateFlow<Boolean> = _isFilterLoading.asStateFlow()

    private val _filterError = MutableStateFlow<String?>(null)
    val filterError: StateFlow<String?> = _filterError.asStateFlow()

    private val _isFilterDialogVisible = MutableStateFlow(false)
    val isFilterDialogVisible: StateFlow<Boolean> = _isFilterDialogVisible.asStateFlow()

    private val _filterState = MutableStateFlow(OrderFilterState())
    val filterState: StateFlow<OrderFilterState> = _filterState.asStateFlow()

    private val _filterCaption: MutableStateFlow<String> = MutableStateFlow("Još uvek nema primenjenih filtera")
    val filterCaption: StateFlow<String> = _filterCaption.asStateFlow()

    init {
        loadMyOrders()
    }

    fun loadMyOrders() {
        viewModelScope.launch {
            _isLoading.value = true
            clearError()

            orderRepository.findMyOrders()
                .onSuccess { orders -> orderStore.setOrders(orders) }
                .onError { error -> _error.value = error.toFindMyOrdersMessage() }

            _isLoading.value = false
        }
    }
    fun clearError() { _error.value = null }


    fun showMyOrders() { _viewMode.value = OrdersViewMode.MY_ORDERS }
    fun showFilteredOrders() { _viewMode.value = OrdersViewMode.FILTERED }

    fun showFilterDialog() { _isFilterDialogVisible.value = true }
    fun hideFilterDialog() {
        _isFilterDialogVisible.value = false
        _filterError.value = null
    }

    fun onBookIdFilterToggle(enabled: Boolean) {
        _filterState.update {
            it.copy(filterByBookId = enabled, filterByUserId = if (enabled) false else it.filterByUserId)
        }
    }

    fun onUserIdFilterToggle(enabled: Boolean) {
        _filterState.update {
            it.copy(filterByUserId = enabled, filterByBookId = if (enabled) false else it.filterByBookId)
        }
    }

    fun onBookIdChange(value: String) {
        _filterState.update { it.copy(bookId = value) }
    }

    fun onUserIdChange(value: String) {
        _filterState.update { it.copy(userId = value) }
    }

    fun applyFilter() {
        val state = _filterState.value
        val bookId = state.bookId.toLongOrNull()
        val userId = state.userId.toLongOrNull()

        if (state.filterByBookId && bookId == null) {
            _filterError.value = "Unesite validan ID knjige"
            return
        }
        if (state.filterByUserId && userId == null) {
            _filterError.value = "Unesite validan ID korisnika"
            return
        }

        viewModelScope.launch {
            _isFilterLoading.value = true
            _filterError.value = null

            val filterTitle: String = when {
                state.filterByBookId -> "Sve porudžbine knjig: $bookId"
                state.filterByUserId -> "Sve porudžbine korisnina: $userId"
                else -> "Sve porudžbine"
            }

            val result = when {
                state.filterByBookId -> orderRepository.findByBookId(bookId!!)
                state.filterByUserId -> orderRepository.findByUserId(userId!!)
                else -> orderRepository.findAll()
            }

            result
                .onSuccess { orders ->
                    _filterCaption.value = filterTitle
                    _filteredOrders.value = orders
                    _viewMode.value = OrdersViewMode.FILTERED
                    _isFilterDialogVisible.value = false
                }
                .onError { error -> _filterError.value = error.toFindOrdersMessage() }

            _isFilterLoading.value = false
        }
    }
}