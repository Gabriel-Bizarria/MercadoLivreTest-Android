package br.com.devtest.mercadolivre.data.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Conditions(

	@SerialName("start_time")
	val startTime: String? = null,

	@SerialName("eligible")
	val eligible: Boolean? = null,

	@SerialName("context_restrictions")
	val contextRestrictions: List<String?>? = null,

	@SerialName("end_time")
	val endTime: String? = null
)