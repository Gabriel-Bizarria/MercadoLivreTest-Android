package br.com.devtest.mercadolivre.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.devtest.mercadolivre.data.utils.onFailure
import br.com.devtest.mercadolivre.data.utils.onSuccess
import br.com.devtest.mercadolivre.domain.repository.ProductDetailRepository
import br.com.devtest.mercadolivre.ui.models.ProductDetailUiModel
import br.com.devtest.mercadolivre.ui.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductDetailViewModel(
    private val repository: ProductDetailRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val itemId: String = checkNotNull(savedStateHandle["itemId"])

    private val _uiState = MutableStateFlow<UiState<ProductDetailUiModel>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun fetchProductDetail() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            repository.getProductDetails(itemId)
                .onSuccess { data ->
                    _uiState.value = UiState.Success(data)
                }
                .onFailure { error, code ->
                    _uiState.value = UiState.Error(error, code)
                }
        }
    }
}