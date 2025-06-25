package br.com.devtest.mercadolivre.ui.models

import br.com.devtest.mercadolivre.data.models.search.ResultsItem
import br.com.devtest.mercadolivre.utils.forceHttps
import br.com.devtest.mercadolivre.utils.toBigDecimalMonetary
import java.math.BigDecimal
import kotlin.collections.orEmpty

data class ProductListItemUiModel(
    val id: String,
    val title: String,
    val price: BigDecimal,
    val isFreeShipping: Boolean,
    val originalPrice: BigDecimal? = null,
    val currencyId: String? = null,
    val productImage: String? = null,
    val attributes: List<AttributeUiModel> = emptyList(),
) {
    companion object {
        internal fun ResultsItem?.toProductUiModel(): ProductListItemUiModel {
            return ProductListItemUiModel(
                id = this?.id.orEmpty(),
                title = this?.title.orEmpty(),
                attributes = this?.attributes?.map {
                    AttributeUiModel(
                        type = it?.id.orEmpty(),
                        value = it?.valueName
                    )
                }.orEmpty(),
                price = (this?.price ?: 0.0).toBigDecimalMonetary(),
                originalPrice = this?.originalPrice?.toBigDecimalMonetary(),
                currencyId = this?.currencyId,
                productImage = this?.thumbnail.forceHttps(),
                isFreeShipping = this?.shipping?.freeShipping == true
            )
        }
    }
}
