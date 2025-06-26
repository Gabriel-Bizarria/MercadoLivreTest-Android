package br.com.devtest.mercadolivre.utils

/**
 * Returns a copy of the string with the first occurrence of "http://" replaced by "https://".
 * If the string is null, returns null.
 */
fun String?.forceHttps(): String? {
    return this?.replaceFirst("http://", "https://")
}

/**
 * Sanitizes the string by removing all non-alphanumeric characters (except spaces),
 * collapsing multiple spaces into one, trimming, and converting to lowercase.
 * If the string is null, returns null.
 */
fun String?.sanitize(): String {
    return this?.trim()
        ?.map { c ->
            when {
                c.isLetterOrDigit() || c == ' ' -> c
                else -> ' '
            }
        }
        ?.joinToString("")
        ?.replace(Regex("\\s+"), " ")
        ?.let { java.text.Normalizer.normalize(it, java.text.Normalizer.Form.NFD) }
        ?.replace(Regex("\\p{InCombiningDiacriticalMarks}+"), "")
        .orEmpty()
}