package br.com.devtest.mercadolivre.data.models.search

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ProductInfoItem(

	@SerialName("score")
	val score: Int? = null,

	@SerialName("id")
	val id: String? = null,

	@SerialName("status")
	val status: String? = null
)