package br.com.devtest.mercadolivre.ui.navigation

import kotlinx.serialization.Serializable

sealed class NavigationRoutes {
    @Serializable
    object SearchScreen

    @Serializable
    object SearchResultScreen

    @Serializable
    data class ProductDetailScreen(
        val itemId: String
    )
}