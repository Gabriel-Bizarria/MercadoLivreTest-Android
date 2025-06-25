package br.com.devtest.mercadolivre.data.models.productdetails

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class VariationsItem(

	@SerialName("price")
	val price: Int? = null,

	@SerialName("catalog_product_id")
	val catalogProductId: String? = null,

	@SerialName("id")
	val id: Long? = null,

	@SerialName("picture_ids")
	val pictureIds: List<String?>? = null,

	@SerialName("attribute_combinations")
	val attributeCombinations: List<AttributeCombinationsItem?>? = null
)