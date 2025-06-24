package br.com.devtest.mercadolivre.utils

import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

/**
 * Converts a BigDecimal to a currency string based on the provided currency ID.
 * If the currency ID is not recognized, it defaults to Brazilian Real (BRL).
 *
 * @param currencyId The currency ID to format the BigDecimal. Defaults to "BRL".
 * @return A formatted currency string.
 */
fun BigDecimal.toCurrencyString(currencyId: String? = "BRL"): String {
    val currency = Currency.getInstance(currencyId ?: "BRL")
    val locale = when (currency.currencyCode) {
        "ARS" -> Locale("es", "AR")
        else -> Locale("pt", "BR")
    }
    val noCentsCurrencies = setOf("ARS")
    val format = NumberFormat.getCurrencyInstance(locale)
    format.currency = currency
    if (currency.currencyCode in noCentsCurrencies) {
        format.maximumFractionDigits = 0
        format.minimumFractionDigits = 0
    }
    return format.format(this)
}

/**
 * Converts a Long value representing a monetary amount (in cents) to a BigDecimal.
 * The value is divided by 100 to convert it to the standard monetary format.
 *
 * @return A BigDecimal representation of the monetary value.
 */
fun Double.toBigDecimalMonetary(): BigDecimal {
    return BigDecimal.valueOf(this).setScale(2, java.math.RoundingMode.HALF_EVEN)
}