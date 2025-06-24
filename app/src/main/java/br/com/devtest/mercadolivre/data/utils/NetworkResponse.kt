package br.com.devtest.mercadolivre.data.utils

sealed class NetworkResponse<out T> {
    data class Success<out T>(val data: T) : NetworkResponse<T>()
    data class GenericError(val message: String) : NetworkResponse<Nothing>()
    data class NetworkError(val message: String, val code: Int? = null) : NetworkResponse<Nothing>()
}

inline fun <T> NetworkResponse<T>.onSuccess(action: (T) -> Unit): NetworkResponse<T> {
    if (this is NetworkResponse.Success) action(data)
    return this
}

inline fun <T> NetworkResponse<T>.onFailure(action: (String, Int?) -> Unit): NetworkResponse<T> {
    when (this) {
        is NetworkResponse.GenericError -> action(this.message, null)
        is NetworkResponse.NetworkError -> action(this.message, this.code)
        else -> {}
    }
    return this
}