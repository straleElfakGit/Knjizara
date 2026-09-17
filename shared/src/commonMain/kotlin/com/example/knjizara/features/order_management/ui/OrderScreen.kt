package com.example.knjizara.features.order_management.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.knjizara.features.order_management.OrderViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import androidx.compose.material3.OutlinedButton
import com.example.knjizara.features.order_management.dto.OrdersViewMode

@OptIn(KoinExperimentalAPI::class)
@Composable
fun OrdersScreen(
    viewModel: OrderViewModel = koinViewModel<OrderViewModel>(),
    isAdmin: Boolean
) {
    val viewMode by viewModel.viewMode.collectAsState()

    val myOrders by viewModel.orders.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    val filteredOrders by viewModel.filteredOrders.collectAsState()
    val isFilterLoading by viewModel.isFilterLoading.collectAsState()

    val isFilterDialogVisible by viewModel.isFilterDialogVisible.collectAsState()
    val filterState by viewModel.filterState.collectAsState()
    val filterError by viewModel.filterError.collectAsState()

    val filteredOrdersTitle by viewModel.filterCaption.collectAsState()


    Column(modifier = Modifier.fillMaxSize()) {
        if (isAdmin) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (viewMode == OrdersViewMode.MY_ORDERS) {
                    Button(onClick = {}, modifier = Modifier.weight(1f)) { Text("Moje") }
                } else {
                    OutlinedButton(onClick = { viewModel.showMyOrders() }, modifier = Modifier.weight(1f)) { Text("Moje") }
                }
                if (viewMode == OrdersViewMode.FILTERED) {
                    Button(onClick = {}, modifier = Modifier.weight(1f)) { Text("Filtrirane") }
                } else {
                    OutlinedButton(onClick = { viewModel.showFilteredOrders() }, modifier = Modifier.weight(1f)) { Text("Filtrirane") }
                }
                OutlinedButton(onClick = { viewModel.showFilterDialog() }) { Text("Filter") }
            }
        }

        when (viewMode) {
            OrdersViewMode.MY_ORDERS -> OrdersListContent(
                title = "Moje porudžbine",
                orders = myOrders,
                isLoading = isLoading,
                error = error,
                onRetry = { viewModel.loadMyOrders() }
            )
            OrdersViewMode.FILTERED -> OrdersListContent(
                title = filteredOrdersTitle,
                orders = filteredOrders,
                isLoading = isFilterLoading,
                error = null,
                onRetry = { viewModel.showFilterDialog() }
            )
        }
    }

    if (isFilterDialogVisible) {
        OrderFilterDialog(
            filterState = filterState,
            isLoading = isFilterLoading,
            error = filterError,
            onBookIdFilterToggle = viewModel::onBookIdFilterToggle,
            onUserIdFilterToggle = viewModel::onUserIdFilterToggle,
            onBookIdChange = viewModel::onBookIdChange,
            onUserIdChange = viewModel::onUserIdChange,
            onApply = viewModel::applyFilter,
            onDismiss = viewModel::hideFilterDialog
        )
    }
}