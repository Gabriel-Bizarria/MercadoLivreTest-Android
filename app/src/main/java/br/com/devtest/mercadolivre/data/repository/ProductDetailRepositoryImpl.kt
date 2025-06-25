package br.com.devtest.mercadolivre.data.repository

import br.com.devtest.mercadolivre.data.datasource.service.ApiService
import br.com.devtest.mercadolivre.data.utils.NetworkResponse
import br.com.devtest.mercadolivre.domain.repository.ProductDetailRepository
import br.com.devtest.mercadolivre.ui.models.AttributeUiModel
import br.com.devtest.mercadolivre.ui.models.ProductDetailUiModel
import br.com.devtest.mercadolivre.utils.toBigDecimalMonetary
import kotlin.collections.orEmpty

class ProductDetailRepositoryImpl(
    private val apiService: ApiService
): ProductDetailRepository {
    override suspend fun getProductDetails(productId: String): NetworkResponse<ProductDetailUiModel> {
        val detailsRequest =  apiService.queryProductDetails(productId)
        val descriptionRequest = apiService.queryProductDescription(productId)

        return if (detailsRequest is NetworkResponse.Success && descriptionRequest is NetworkResponse.Success) {
            val details = detailsRequest.data
            val description = descriptionRequest.data

            val productUiModel = ProductDetailUiModel(
                id = details.id.orEmpty(),
                title = details.title.orEmpty(),
                price = (details.price ?: 0.0).toBigDecimalMonetary(),
                originalPrice = (details.originalPrice ?: 0.0).toBigDecimalMonetary(),
                images = details.pictures?.map { it?.secureUrl.orEmpty() }.orEmpty(),
                freeShipping = details.shipping?.freeShipping == true,
                attributes = details.attributes?.map {
                    AttributeUiModel(
                        type = it?.id.orEmpty(),
                        value = it?.valueName
                    )
                }.orEmpty(),
                currencyId = details.currencyId,
                description = description.plainText
            )
            return NetworkResponse.Success(productUiModel)
        } else {
            return when {
                detailsRequest is NetworkResponse.GenericError -> detailsRequest
                descriptionRequest is NetworkResponse.GenericError -> descriptionRequest
                detailsRequest is NetworkResponse.NetworkError -> detailsRequest
                descriptionRequest is NetworkResponse.NetworkError -> descriptionRequest
                else -> NetworkResponse.GenericError(message = "Unknown error occurred")
            }
        }
    }


}