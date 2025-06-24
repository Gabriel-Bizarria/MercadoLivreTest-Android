package br.com.devtest.mercadolivre.domain.repository

import br.com.devtest.mercadolivre.data.utils.NetworkResponse
import br.com.devtest.mercadolivre.ui.models.ProductUiModel

interface SearchRepository {
    suspend fun getSearchResults(query: String): NetworkResponse<List<ProductUiModel>>
}