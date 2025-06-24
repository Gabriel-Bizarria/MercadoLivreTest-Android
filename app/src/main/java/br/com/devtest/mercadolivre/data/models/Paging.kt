package br.com.devtest.mercadolivre.data.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Paging(

	@SerialName("total")
	val total: Int? = null,

	@SerialName("offset")
	val offset: Int? = null,

	@SerialName("limit")
	val limit: Int? = null,

	@SerialName("primary_results")
	val primaryResults: Int? = null
)