package br.com.devtest.mercadolivre.data.models.productdetails

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class PicturesItem(

	@SerialName("size")
	val size: String? = null,

	@SerialName("secure_url")
	val secureUrl: String? = null,

	@SerialName("id")
	val id: String? = null,

	@SerialName("url")
	val url: String? = null,

	@SerialName("max_size")
	val maxSize: String? = null,

	@SerialName("quality")
	val quality: String? = null
)