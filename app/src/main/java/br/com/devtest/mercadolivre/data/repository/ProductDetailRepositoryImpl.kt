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
) : ProductDetailRepository {
    override suspend fun getProductDetails(productId: String): NetworkResponse<ProductDetailUiModel> {
        val detailsRequest = apiService.queryProductDetails(productId)
        val descriptionRequest = apiService.queryProductDescription(productId)

        return if (detailsRequest is NetworkResponse.Success && descriptionRequest is NetworkResponse.Success) {
            val details = detailsRequest.data
            val description = descriptionRequest.data

            if (details.id.isNullOrEmpty() || details.title.isNullOrEmpty() || details.price == null) {
                return NetworkResponse.GenericError("Product details not found")
            }

            val productUiModel = ProductDetailUiModel(
                id = details.id,
                title = details.title,
                price = details.price.toBigDecimalMonetary(),
                originalPrice = (details.originalPrice)?.toBigDecimalMonetary(),
                images = details.pictures?.mapNotNull { it?.secureUrl } ?: emptyList(),
                freeShipping = details.shipping?.freeShipping == true,
                attributes = details.attributes?.map {
                    AttributeUiModel(
                        attributeId = it?.id.orEmpty(),
                        attributeName = it?.name,
                        valueName = it?.valueName
                    )
                }.orEmpty(),
                currencyId = details.currencyId,
                description = description.plainText,
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