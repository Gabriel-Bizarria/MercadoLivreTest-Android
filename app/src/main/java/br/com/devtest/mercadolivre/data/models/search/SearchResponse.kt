package br.com.devtest.mercadolivre.data.models.search

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class SearchResponse(

	@SerialName("available_sorts")
	val availableSorts: List<AvailableSortsItem?>? = null,

	@SerialName("pdp_tracking")
	val pdpTracking: PdpTracking? = null,

	@SerialName("query")
	val query: String? = null,

	@SerialName("available_filters")
	val availableFilters: List<AvailableFiltersItem?>? = null,

	@SerialName("site_id")
	val siteId: String? = null,

	@SerialName("paging")
	val paging: Paging? = null,

	@SerialName("country_default_time_zone")
	val countryDefaultTimeZone: String? = null,

	@SerialName("sort")
	val sort: Sort? = null,

	@SerialName("filters")
	val filters: List<FiltersItem?>? = null,

	@SerialName("results")
	val results: List<ResultsItem?>? = null
)