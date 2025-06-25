package br.com.devtest.mercadolivre.data.models.search

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Installments(

	@SerialName("amount")
	val amount: Double? = null,

	@SerialName("metadata")
	val metadata: Metadata? = null,

	@SerialName("quantity")
	val quantity: Int? = null,

	@SerialName("rate")
	val rate: Double? = null,

	@SerialName("currency_id")
	val currencyId: String? = null
)