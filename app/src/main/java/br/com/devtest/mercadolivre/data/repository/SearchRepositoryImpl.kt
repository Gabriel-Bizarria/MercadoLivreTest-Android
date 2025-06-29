package br.com.devtest.mercadolivre.data.repository

import br.com.devtest.mercadolivre.data.datasource.service.ApiService
import br.com.devtest.mercadolivre.data.utils.NetworkResponse
import br.com.devtest.mercadolivre.domain.repository.SearchRepository
import br.com.devtest.mercadolivre.ui.models.ProductListItemUiModel
import br.com.devtest.mercadolivre.ui.models.ProductListItemUiModel.Companion.toProductUiModel


class SearchRepositoryImpl(private val apiService: ApiService) : SearchRepository {
    override suspend fun getSearchResults(query: String): NetworkResponse<List<ProductListItemUiModel>> {
        val result = apiService.queryProducts(query)
        return when (result) {
            is NetworkResponse.Success -> {
                val list = result.data.results
                    ?.filterNotNull()
                    ?.filter { it.id != null && !it.title.isNullOrBlank() && it.price != null }
                    ?.map { it.toProductUiModel() }
                    .orEmpty()
                NetworkResponse.Success(list)
            }
            is NetworkResponse.GenericError -> result

            is NetworkResponse.NetworkError -> result
        }
    }
}