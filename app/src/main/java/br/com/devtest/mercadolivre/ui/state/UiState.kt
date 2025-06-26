package br.com.devtest.mercadolivre.ui.state

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String? = null, val code: Int? = null, val messageStringRes: Int? = null,) : UiState<Nothing>()
}