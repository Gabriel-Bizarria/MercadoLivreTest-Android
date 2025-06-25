package br.com.devtest.mercadolivre.data.models.productdetails

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Shipping(

	@SerialName("mode")
	val mode: String? = null,

	@SerialName("free_shipping")
	val freeShipping: Boolean? = null,

	@SerialName("local_pick_up")
	val localPickUp: Boolean? = null,

	@SerialName("tags")
	val tags: List<String?>? = null,

	@SerialName("logistic_type")
	val logisticType: String? = null,

	@SerialName("store_pick_up")
	val storePickUp: Boolean? = null
)