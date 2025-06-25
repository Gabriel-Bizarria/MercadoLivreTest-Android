package br.com.devtest.mercadolivre.data.models.search

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class PdpTracking(

	@SerialName("product_info")
	val productInfo: List<ProductInfoItem?>? = null,

	@SerialName("group")
	val group: Boolean? = null
)