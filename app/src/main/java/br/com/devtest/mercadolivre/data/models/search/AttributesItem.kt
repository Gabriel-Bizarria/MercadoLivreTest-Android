package br.com.devtest.mercadolivre.data.models.search

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class AttributesItem(

	@SerialName("attribute_group_id")
	val attributeGroupId: String? = null,

	@SerialName("value_type")
	val valueType: String? = null,

	@SerialName("values")
	val values: List<ValuesItem?>? = null,

	@SerialName("name")
	val name: String? = null,

	@SerialName("attribute_group_name")
	val attributeGroupName: String? = null,

	@SerialName("id")
	val id: String? = null,

	@SerialName("value_id")
	val valueId: String? = null,

	@SerialName("source")
	val source: String? = null,

	@SerialName("value_name")
	val valueName: String? = null
)