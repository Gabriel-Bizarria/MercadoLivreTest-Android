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
import br.com.devtest.mercadolivre.ui.commons.components.SearchBar
import br.com.devtest.mercadolivre.ui.models.ProductUiModel
import br.com.devtest.mercadolivre.ui.viewmodels.SearchViewModel
import br.com.devtest.mercadolivre.ui.state.UiState

@Composable
fun SearchResultListScreen(
    viewModel: SearchViewModel,
    modifier: Modifier = Modifier
) {
    val query = viewModel.queryInput.collectAsStateWithLifecycle()
    val state = viewModel.searchUiState.collectAsStateWithLifecycle()
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
                Text(
                    text = (state.value as UiState.Error).message,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            dimensionResource(R.dimen.padding_md)
                        )
                )
            }

            is UiState.Success -> {
                val products = (state.value as UiState.Success<List<ProductUiModel>>).data

                if(products.isEmpty()) {
                    Text(
                        text = stringResource(R.string.no_product_found),
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                } else {
                    val listState = rememberLazyListState()
                    ProductsList(list = products, listState = listState)
                }
            }
        }
        
        SearchBar(
            placeholder = "Search for products",
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
    list: List<ProductUiModel>,
    listState: LazyListState,
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
                productItem = it, modifier = Modifier.fillMaxSize()
            )
            HorizontalDivider()
        }
    }
}