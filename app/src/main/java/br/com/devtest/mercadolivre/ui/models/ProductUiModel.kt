package br.com.devtest.mercadolivre.ui.models

import br.com.devtest.mercadolivre.data.models.ResultsItem
import br.com.devtest.mercadolivre.utils.forceHttps
import br.com.devtest.mercadolivre.utils.toBigDecimalMonetary
import java.math.BigDecimal

data class ProductUiModel(
    val id: String,
    val title: String,
    val price: BigDecimal,
    val isFreeShipping: Boolean,
    val originalPrice: BigDecimal? = null,
    val brand: String? = null,
    val currencyId: String? = null,
    val productImage: String? = null,
) {
    companion object {
        internal fun ResultsItem?.toProductUiModel(): ProductUiModel {
            return ProductUiModel(
                id = this?.id.orEmpty(),
                title = this?.title.orEmpty(),
                brand = this?.attributes?.find { it?.id == "BRAND" }?.valueName,
                price = (this?.price ?: 0.0).toBigDecimalMonetary(),
                originalPrice = this?.originalPrice?.toBigDecimalMonetary(),
                currencyId = this?.currencyId,
                productImage = this?.thumbnail.forceHttps(),
                isFreeShipping = this?.shipping?.freeShipping == true
            )
        }
    }
}
