package br.com.devtest.mercadolivre.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import br.com.devtest.mercadolivre.data.utils.onFailure
import br.com.devtest.mercadolivre.data.utils.onSuccess
import br.com.devtest.mercadolivre.domain.repository.ProductDetailRepository
import br.com.devtest.mercadolivre.ui.models.ProductDetailUiModel
import br.com.devtest.mercadolivre.ui.state.UiState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductDetailViewModel(
    private val repository: ProductDetailRepository,
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val itemId: String = checkNotNull(savedStateHandle["itemId"])

    private val _uiState = MutableStateFlow<UiState<ProductDetailUiModel>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val viewModelCustomScope = CoroutineScope(SupervisorJob() + dispatcher)

    fun fetchProductDetail() {
        viewModelCustomScope.launch {
            _uiState.value = UiState.Loading
            repository.getProductDetails(itemId)
                .onSuccess { data ->
                    _uiState.value = UiState.Success(data)
                }
                .onFailure { error, code ->
                    _uiState.value = UiState.Error(message = error, code =code)
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelCustomScope.cancel()
    }
}