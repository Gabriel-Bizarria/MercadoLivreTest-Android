package br.com.devtest.mercadolivre.data.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Shipping(

	@SerialName("mode")
	val mode: String? = null,

	@SerialName("free_shipping")
	val freeShipping: Boolean? = null,


	@SerialName("shipping_score")
	val shippingScore: Int? = null,

	@SerialName("store_pick_up")
	val storePickUp: Boolean? = null,

	@SerialName("logistic_type")
	val logisticType: String? = null,

	@SerialName("tags")
	val tags: List<String?>? = null
)