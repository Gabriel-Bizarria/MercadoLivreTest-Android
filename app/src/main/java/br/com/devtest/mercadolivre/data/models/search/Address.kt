package br.com.devtest.mercadolivre.data.models.search

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Address(

	@SerialName("city_name")
	val cityName: String? = null,

	@SerialName("state_name")
	val stateName: String? = null,

	@SerialName("state_id")
	val stateId: String? = null,

	@SerialName("city_id")
	val cityId: String? = null
)