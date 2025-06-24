package br.com.devtest.mercadolivre.data.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class FiltersItem(

	@SerialName("values")
	val values: List<ValuesItem?>? = null,

	@SerialName("name")
	val name: String? = null,

	@SerialName("id")
	val id: String? = null,

	@SerialName("type")
	val type: String? = null
)