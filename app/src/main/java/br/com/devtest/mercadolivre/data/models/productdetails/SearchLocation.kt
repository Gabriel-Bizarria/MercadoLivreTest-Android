package br.com.devtest.mercadolivre.data.models.productdetails

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class SearchLocation(

	@SerialName("city")
	val city: City? = null,

	@SerialName("neighborhood")
	val neighborhood: Neighborhood? = null,

	@SerialName("state")
	val state: State? = null
)