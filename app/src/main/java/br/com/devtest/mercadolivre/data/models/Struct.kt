package br.com.devtest.mercadolivre.data.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Struct(

	@SerialName("number")
	val number: Int? = null,

	@SerialName("unit")
	val unit: String? = null
)