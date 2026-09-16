package com.example.knjizara.di

import com.example.knjizara.features.auth.AuthRepository
import com.example.knjizara.features.auth.LoginViewModel
import com.example.knjizara.features.auth.RegisterViewModel
import com.example.knjizara.features.auth.SessionManager
import com.example.knjizara.features.auth.token_manager.TokenStorage
import com.example.knjizara.features.auth.token_manager.TokenStorageImpl
import com.example.knjizara.features.book_management.AdminBookViewModel
import com.example.knjizara.features.book_management.BookRepository
import com.example.knjizara.features.book_management.BookViewModel
import com.example.knjizara.networking.apis.AuthApi
import com.example.knjizara.networking.apis.BookApi
import com.example.knjizara.networking.createHttpClient
import com.example.knjizara.ui.StartupViewModel
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {
    single<TokenStorage> { TokenStorageImpl(get()) }
    single { SessionManager() }
    single { createHttpClient(get(), get()) }
    viewModelOf(::StartupViewModel)

    single { AuthApi(get()) }
    single { AuthRepository(get(), get(), get()) }
    viewModelOf(::LoginViewModel)
    viewModelOf(::RegisterViewModel)

    single { BookApi(get()) }
    single { BookRepository(get()) }
    viewModelOf(::BookViewModel)
    viewModelOf(::AdminBookViewModel)
}