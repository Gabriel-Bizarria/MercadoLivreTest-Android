package br.com.devtest.mercadolivre.data.models.productdetails

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class AttributeCombinationsItem(

	@SerialName("value_type")
	val valueType: String? = null,

	@SerialName("values")
	val values: List<ValuesItem?>? = null,

	@SerialName("name")
	val name: String? = null,

	@SerialName("id")
	val id: String? = null,

	@SerialName("value_id")
	val valueId: String? = null,

	@SerialName("value_name")
	val valueName: String? = null
)