package br.com.devtest.mercadolivre.data.models.productdescription

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ProductDescriptionResponse(

	@SerialName("last_updated")
	val lastUpdated: String? = null,

	@SerialName("plain_text")
	val plainText: String? = null,

	@SerialName("date_created")
	val dateCreated: String? = null,

	@SerialName("text")
	val text: String? = null,

	@SerialName("snapshot")
	val snapshot: Snapshot? = null
)