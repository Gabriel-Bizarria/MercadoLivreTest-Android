package br.com.devtest.mercadolivre.domain.repository

import br.com.devtest.mercadolivre.data.utils.NetworkResponse
import br.com.devtest.mercadolivre.ui.models.ProductDetailUiModel

interface ProductDetailRepository {
    suspend fun getProductDetails(productId: String): NetworkResponse<ProductDetailUiModel>
}