package br.com.devtest.mercadolivre.ui.viewmodels

import androidx.lifecycle.ViewModel
import br.com.devtest.mercadolivre.data.utils.onFailure
import br.com.devtest.mercadolivre.data.utils.onSuccess
import br.com.devtest.mercadolivre.domain.repository.SearchRepository
import br.com.devtest.mercadolivre.ui.models.ProductUiModel
import br.com.devtest.mercadolivre.ui.state.UiState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchRepository: SearchRepository,
    dispatcher: CoroutineDispatcher = Dispatchers.Main
) : ViewModel() {

    private val _queryInput = MutableStateFlow<String>("")
    val queryInput = _queryInput.asStateFlow()

    private val _searchUiState = MutableStateFlow<UiState<List<ProductUiModel>>>(UiState.Loading)
    val searchUiState = _searchUiState.asStateFlow()

    private val viewModelCustomScope = CoroutineScope(SupervisorJob() + dispatcher)

    fun setQueryInput(query: String) {
        _queryInput.value = query
    }

    fun fetchSearchResults() {
        viewModelCustomScope.launch {
            _searchUiState.value = UiState.Loading
            searchRepository.getSearchResults(queryInput.value)
                .onSuccess { data ->
                    _searchUiState.value = UiState.Success(data)
                }
                .onFailure { error, _ ->
                    _searchUiState.value = UiState.Error(
                        message = error
                    )
                }
        }

    }
}