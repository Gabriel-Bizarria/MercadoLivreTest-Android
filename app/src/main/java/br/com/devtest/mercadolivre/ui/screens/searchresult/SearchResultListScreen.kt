package br.com.devtest.mercadolivre.ui.screens.searchresult

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.devtest.mercadolivre.R
import br.com.devtest.mercadolivre.ui.commons.components.ErrorScreen
import br.com.devtest.mercadolivre.ui.commons.components.SearchBar
import br.com.devtest.mercadolivre.ui.models.ProductListItemUiModel
import br.com.devtest.mercadolivre.ui.viewmodels.SearchViewModel
import br.com.devtest.mercadolivre.ui.state.UiState

@Composable
fun SearchResultListScreen(
    viewModel: SearchViewModel,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val query = viewModel.queryInput.collectAsStateWithLifecycle()
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        when (state.value) {
            is UiState.Loading -> {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier
                        .size(64.dp)
                        .align(Alignment.Center)
                )
            }

            is UiState.Error -> {
                val error = state.value as UiState.Error

                ErrorScreen(
                    error = error,
                    modifier = Modifier
                        .padding(
                            dimensionResource(R.dimen.padding_md)
                        ).align(Alignment.Center)
                )
            }

            is UiState.Success -> {
                val products = (state.value as UiState.Success<List<ProductListItemUiModel>>).data

                if (products.isEmpty()) {
                    Text(
                        text = stringResource(R.string.no_product_found),
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                } else {
                    val listState = rememberLazyListState()
                    ProductsList(list = products, listState = listState, onItemClick = onItemClick)
                }
            }
        }

        SearchBar(
            placeholder = stringResource(R.string.search_one_item),
            query = query.value,
            onQueryChange = {
                viewModel.setQueryInput(it)
            },
            onSearch = {
                viewModel.fetchSearchResults()
            },
            modifier = modifier
                .padding(dimensionResource(R.dimen.padding_sm))
                .zIndex(1f)
        )
    }
}

@Composable
private fun ProductsList(
    list: List<ProductListItemUiModel>,
    listState: LazyListState,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(
            top = 100.dp
        ),
        modifier = modifier
    ) {
        items(
            items = list,
            key = { it.id }
        ) {
            SearchProductItem(
                onItemClick = onItemClick,
                productItem = it,
                modifier = Modifier.fillMaxSize()
            )
            HorizontalDivider()
        }
    }
}