package com.example.knjizara.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.knjizara.features.auth.dto.UserDto
import com.example.knjizara.features.auth.dto.UserRole
import com.example.knjizara.features.book_management.BookViewModel
import com.example.knjizara.features.book_management.ui.BookScreen
import com.example.knjizara.features.order_management.OrderViewModel
import com.example.knjizara.features.order_management.ui.OrdersScreen
import com.example.knjizara.navigation.enums.HomeTab
import knjizara.shared.generated.resources.Res
import knjizara.shared.generated.resources.compose_multiplatform
import knjizara.shared.generated.resources.open_book_icon
import knjizara.shared.generated.resources.shopping_cart_icon
import knjizara.shared.generated.resources.user_profile_icon
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun HomeScreen(
    user: UserDto,
    bookViewModel: BookViewModel = koinViewModel<BookViewModel> (),
    orderViewModel: OrderViewModel = koinViewModel<OrderViewModel> (),
    onLogout: () -> Unit = {}
) {
    var selectedTab by rememberSaveable { mutableStateOf(HomeTab.BOOKS) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedTab == HomeTab.BOOKS,
                    onClick = { selectedTab = HomeTab.BOOKS },
                    icon = { Icon(
                        painter = painterResource(Res.drawable.open_book_icon),
                        contentDescription = HomeTab.BOOKS.label,
                        modifier = Modifier.size(60.dp)
                    ) },
                    label = { Text(HomeTab.BOOKS.label) }
                )
                NavigationBarItem(
                    selected = selectedTab == HomeTab.ORDERS,
                    onClick = { selectedTab = HomeTab.ORDERS },
                    icon = { Icon(
                        painter = painterResource(Res.drawable.shopping_cart_icon),
                        contentDescription = HomeTab.ORDERS.label,
                        modifier = Modifier.size(60.dp)
                    ) },
                    label = { Text(HomeTab.ORDERS.label) }
                )
                NavigationBarItem(
                    selected = selectedTab == HomeTab.PROFILE,
                    onClick = { selectedTab = HomeTab.PROFILE },
                    icon = { Icon(
                        painter = painterResource(Res.drawable.user_profile_icon),
                        contentDescription = HomeTab.PROFILE.label,
                        modifier = Modifier.size(60.dp)
                    ) },
                    label = { Text(HomeTab.PROFILE.label) }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                HomeTab.BOOKS -> BookScreen(bookViewModel, user.role == UserRole.ADMIN)
                HomeTab.ORDERS -> OrdersScreen(viewModel = orderViewModel, user.role == UserRole.ADMIN)
                HomeTab.PROFILE -> ProfileScreen(user = user, onLogout = onLogout)
            }
        }
    }
}