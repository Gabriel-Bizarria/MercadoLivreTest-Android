package br.com.devtest.mercadolivre.ui.models

import java.math.BigDecimal

data class ProductDetailUiModel(
    val id: String,
    val title: String,
    val price: BigDecimal,
    val images: List<String>,
    val freeShipping: Boolean,
    val attributes: List<AttributeUiModel> = emptyList(),
    val originalPrice: BigDecimal? = null,
    val description: String? = null,
    val currencyId: String? = null,
)


