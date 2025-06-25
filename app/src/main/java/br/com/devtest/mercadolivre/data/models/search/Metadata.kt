package br.com.devtest.mercadolivre.data.models.search

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Metadata(

	@SerialName("promotion_type")
	val promotionType: String? = null,

	@SerialName("promotion_offer_type")
	val promotionOfferType: String? = null,

	@SerialName("promotion_id")
	val promotionId: String? = null,

	@SerialName("campaign_id")
	val campaignId: String? = null,

	@SerialName("additional_bank_interest")
	val additionalBankInterest: Boolean? = null,

	@SerialName("meliplus_installments")
	val meliplusInstallments: Boolean? = null,

	@SerialName("order_item_price")
	val orderItemPrice: Double? = null,

	@SerialName("discount_meli_amount")
	val discountMeliAmount: Double? = null,

	@SerialName("funding_mode")
	val fundingMode: String? = null,

	@SerialName("promotion_offer_sub_type")
	val promotionOfferSubType: String? = null,

	@SerialName("campaign_end_date")
	val campaignEndDate: String? = null,

	@SerialName("campaign_discount_percentage")
	val campaignDiscountPercentage: Double? = null,

	@SerialName("experiment_id")
	val experimentId: String? = null,

	@SerialName("variation")
	val variation: String? = null
)