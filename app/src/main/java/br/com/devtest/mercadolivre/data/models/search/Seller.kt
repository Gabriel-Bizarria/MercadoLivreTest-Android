package br.com.devtest.mercadolivre.data.models.search

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Seller(

	@SerialName("nickname")
	val nickname: String? = null,

	@SerialName("id")
	val id: Int? = null
)