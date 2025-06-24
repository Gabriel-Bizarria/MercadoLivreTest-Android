package br.com.devtest.mercadolivre.data.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ValuesItem(

	@SerialName("name")
	val name: String? = null,

	@SerialName("id")
	val id: String? = null,

	@SerialName("results")
	val results: Int? = null,

	@SerialName("path_from_root")
	val pathFromRoot: List<PathFromRootItem?>? = null,

	@SerialName("source")
	val source: String? = null
)