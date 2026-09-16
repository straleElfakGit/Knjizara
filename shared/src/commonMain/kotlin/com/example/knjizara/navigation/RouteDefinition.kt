package com.example.knjizara.navigation

import kotlinx.serialization.Serializable

@Serializable data object AuthGraph
@Serializable data object Login
@Serializable data object Register

@Serializable data object MainGraph
@Serializable data object Home
@Serializable data class BookDetails(val bookId: String)
@Serializable data object Cart
@Serializable data object Profile