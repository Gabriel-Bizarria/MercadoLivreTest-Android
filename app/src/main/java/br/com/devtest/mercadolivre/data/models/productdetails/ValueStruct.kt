package br.com.devtest.mercadolivre.data.models.productdetails

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ValueStruct(

	@SerialName("number")
	val number: Int? = null,

	@SerialName("unit")
	val unit: String? = null
)