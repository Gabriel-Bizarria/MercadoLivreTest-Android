package br.com.devtest.mercadolivre.data.models.productdescription

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Snapshot(

	@SerialName("width")
	val width: Int? = null,

	@SerialName("url")
	val url: String? = null,

	@SerialName("height")
	val height: Int? = null,

	@SerialName("status")
	val status: String? = null
)