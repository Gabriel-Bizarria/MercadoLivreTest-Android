package br.com.devtest.mercadolivre.data.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ResultsItem(

	@SerialName("seller")
	val seller: Seller? = null,

	@SerialName("original_price")
	val originalPrice: Double? = null,

	@SerialName("stop_time")
	val stopTime: String? = null,

	@SerialName("catalog_listing")
	val catalogListing: Boolean? = null,

	@SerialName("buying_mode")
	val buyingMode: String? = null,

	@SerialName("title")
	val title: String? = null,

	@SerialName("domain_id")
	val domainId: String? = null,

	@SerialName("available_quantity")
	val availableQuantity: Int? = null,

	@SerialName("use_thumbnail_id")
	val useThumbnailId: Boolean? = null,

	@SerialName("category_id")
	val categoryId: String? = null,

	@SerialName("shipping")
	val shipping: Shipping? = null,


	@SerialName("installments")
	val installments: Installments? = null,

	@SerialName("price")
	val price: Double? = null,

	@SerialName("official_store_id")
	val officialStoreId: Int? = null,

	@SerialName("id")
	val id: String? = null,

	@SerialName("accepts_mercadopago")
	val acceptsMercadopago: Boolean? = null,

	@SerialName("result_type")
	val resultType: String? = null,

	@SerialName("thumbnail")
	val thumbnail: String? = null,

	@SerialName("sanitized_title")
	val sanitizedTitle: String? = null,

	@SerialName("address")
	val address: Address? = null,

	@SerialName("inventory_id")
	val inventoryId: String? = null,

	@SerialName("catalog_product_id")
	val catalogProductId: String? = null,

	@SerialName("sale_price")
	val salePrice: SalePrice? = null,

	@SerialName("installments_motors")
	val installmentsMotors: @Contextual Any? = null,

	@SerialName("order_backend")
	val orderBackend: Int? = null,

	@SerialName("condition")
	val condition: String? = null,

	@SerialName("thumbnail_id")
	val thumbnailId: String? = null,

	@SerialName("promotions")
	val promotions: @Contextual Any? = null,

	@SerialName("site_id")
	val siteId: String? = null,

	@SerialName("attributes")
	val attributes: List<AttributesItem?>? = null,

	@SerialName("listing_type_id")
	val listingTypeId: String? = null,

	@SerialName("permalink")
	val permalink: String? = null,

	@SerialName("currency_id")
	val currencyId: String? = null,

	@SerialName("official_store_name")
	val officialStoreName: String? = null
)