package br.com.devtest.mercadolivre.utils

fun String?.forceHttps(): String? {
    return this?.replaceFirst("http://", "https://")
}