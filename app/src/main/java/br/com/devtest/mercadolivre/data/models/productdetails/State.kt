package br.com.devtest.mercadolivre.data.models.productdetails

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class State(

	@SerialName("name")
	val name: String? = null,

	@SerialName("id")
	val id: String? = null
)