package br.com.devtest.mercadolivre.ui.screens.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.devtest.mercadolivre.R
import br.com.devtest.mercadolivre.ui.commons.components.SearchBar
import br.com.devtest.mercadolivre.ui.commons.utils.TypeWriterEffect
import br.com.devtest.mercadolivre.ui.viewmodels.SearchViewModel

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    navigateToSearchResults: () -> Unit,
    modifier: Modifier = Modifier
) {
    val queryInput = viewModel.queryInput.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(0.4f)
                .padding(horizontal = dimensionResource(R.dimen.padding_md))
        ) {
            Image(
                painter = painterResource(id = R.drawable.mercado_livre_logo),
                contentDescription = "Search Icon",
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .sizeIn(minHeight = 200.dp)
                .weight(0.6f)
                .padding(horizontal = dimensionResource(R.dimen.padding_md)),
        ) {
            TypeWriterEffect(
                text = stringResource(R.string.what_are_you_looking_for),
            ) {
                Text(
                    text = it,
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.3f)
                )
            }
            Box(
                contentAlignment = Alignment.TopCenter,
                modifier = Modifier
                    .weight(0.7f)
            ) {
                SearchBar(
                    query = queryInput.value,
                    onQueryChange = viewModel::setQueryInput,
                    placeholder = stringResource(R.string.search_one_item),
                    onSearch = {
                        viewModel.fetchSearchResults()
                        navigateToSearchResults.invoke()
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                )
            }
        }
    }
}