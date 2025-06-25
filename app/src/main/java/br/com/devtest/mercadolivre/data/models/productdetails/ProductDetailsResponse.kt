package br.com.devtest.mercadolivre.data.models.productdetails

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ProductDetailsResponse(

	@SerialName("original_price")
	val originalPrice: Double? = null,

	@SerialName("catalog_listing")
	val catalogListing: Boolean? = null,

	@SerialName("buying_mode")
	val buyingMode: String? = null,

	@SerialName("title")
	val title: String? = null,

	@SerialName("pictures")
	val pictures: List<PicturesItem?>? = null,

	@SerialName("domain_id")
	val domainId: String? = null,

	@SerialName("category_id")
	val categoryId: String? = null,

	@SerialName("shipping")
	val shipping: Shipping? = null,

	@SerialName("price")
	val price: Double? = null,

	@SerialName("variations")
	val variations: List<VariationsItem?>? = null,

	@SerialName("base_price")
	val basePrice: Double? = null,

	@SerialName("official_store_id")
	val officialStoreId: Int? = null,

	@SerialName("warranty")
	val warranty: String? = null,

	@SerialName("id")
	val id: String? = null,

	@SerialName("seller_id")
	val sellerId: Int? = null,

	@SerialName("accepts_mercadopago")
	val acceptsMercadopago: Boolean? = null,

	@SerialName("sale_terms")
	val saleTerms: List<SaleTermsItem?>? = null,

	@SerialName("thumbnail")
	val thumbnail: String? = null,

	@SerialName("last_updated")
	val lastUpdated: String? = null,

	@SerialName("initial_quantity")
	val initialQuantity: Int? = null,

	@SerialName("date_created")
	val dateCreated: String? = null,

	@SerialName("catalog_product_id")
	val catalogProductId: String? = null,

	@SerialName("listing_source")
	val listingSource: String? = null,

	@SerialName("automatic_relist")
	val automaticRelist: Boolean? = null,

	@SerialName("seller_address")
	val sellerAddress: SellerAddress? = null,

	@SerialName("tags")
	val tags: List<String?>? = null,

	@SerialName("international_delivery_mode")
	val internationalDeliveryMode: String? = null,

	@SerialName("condition")
	val condition: String? = null,

	@SerialName("thumbnail_id")
	val thumbnailId: String? = null,

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

	@SerialName("status")
	val status: String? = null
)