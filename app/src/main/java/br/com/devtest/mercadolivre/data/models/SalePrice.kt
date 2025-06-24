package br.com.devtest.mercadolivre.data.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class SalePrice(

	@SerialName("payment_method_type")
	val paymentMethodType: String? = null,

	@SerialName("amount")
	val amount: Double? = null,

	@SerialName("metadata")
	val metadata: Metadata? = null,

	@SerialName("regular_amount")
	val regularAmount: Double? = null,

	@SerialName("price_id")
	val priceId: String? = null,

	@SerialName("conditions")
	val conditions: Conditions? = null,

	@SerialName("type")
	val type: String? = null,

	@SerialName("currency_id")
	val currencyId: String? = null
)