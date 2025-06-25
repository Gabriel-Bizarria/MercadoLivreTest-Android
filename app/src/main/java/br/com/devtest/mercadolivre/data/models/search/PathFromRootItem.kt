package br.com.devtest.mercadolivre.data.models.search

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class PathFromRootItem(

	@SerialName("name")
	val name: String? = null,

	@SerialName("id")
	val id: String? = null
)