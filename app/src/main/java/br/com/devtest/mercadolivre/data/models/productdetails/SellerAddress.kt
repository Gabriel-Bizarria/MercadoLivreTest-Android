package br.com.devtest.mercadolivre.data.models.productdetails

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class SellerAddress(

	@SerialName("country")
	val country: Country? = null,

	@SerialName("search_location")
	val searchLocation: SearchLocation? = null,

	@SerialName("city")
	val city: City? = null,

	@SerialName("state")
	val state: State? = null,

	@SerialName("id")
	val id: Int? = null
)