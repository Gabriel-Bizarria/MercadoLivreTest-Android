package br.com.devtest.mercadolivre.ui.viewmodels

import androidx.lifecycle.ViewModel
import br.com.devtest.mercadolivre.R
import br.com.devtest.mercadolivre.data.utils.onFailure
import br.com.devtest.mercadolivre.data.utils.onSuccess
import br.com.devtest.mercadolivre.domain.repository.SearchRepository
import br.com.devtest.mercadolivre.ui.models.ProductListItemUiModel
import br.com.devtest.mercadolivre.ui.state.UiState
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repository: SearchRepository,
    dispatcher: CoroutineDispatcher = Dispatchers.Main
) : ViewModel() {

    private val _queryInput = MutableStateFlow<String>("")
    val queryInput = _queryInput.asStateFlow()

    private val _uiState =
        MutableStateFlow<UiState<List<ProductListItemUiModel>>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val viewModelCustomScope = CoroutineScope(SupervisorJob() + dispatcher)

    fun setQueryInput(query: String) {
        _queryInput.value = query
    }

    fun fetchSearchResults() {
        viewModelCustomScope.launch {
            _uiState.value = UiState.Loading
            repository.getSearchResults(queryInput.value)
                .onSuccess { data ->
                    _uiState.value = UiState.Success(data)
                }
                .onFailure { error, code ->
                    when (code) {
                        HttpStatusCode.NotFound.value -> {
                            _uiState.value = UiState.Error(
                                messageStringRes = R.string.no_product_found,
                                message = error,
                            )
                        }

                        HttpStatusCode.BadRequest.value -> {
                            _uiState.value = UiState.Error(
                                message = error,
                                code = code
                            )
                        }

                        else -> {
                            _uiState.value = UiState.Error(
                                message = error,
                                code = code
                            )
                        }
                    }
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelCustomScope.cancel()
    }
}