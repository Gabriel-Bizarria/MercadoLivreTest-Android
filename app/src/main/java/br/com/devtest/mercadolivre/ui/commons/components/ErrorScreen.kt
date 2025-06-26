package br.com.devtest.mercadolivre.ui.commons.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import br.com.devtest.mercadolivre.R
import br.com.devtest.mercadolivre.ui.state.UiState

@Composable
fun ErrorScreen(
    error: UiState.Error,
    modifier: Modifier = Modifier
) {
    val errorMessage = when {
        error.messageStringRes != null && error.messageStringRes != 0 ->
            stringResource(id = error.messageStringRes)
        !error.message.isNullOrEmpty() -> error.message
        else -> stringResource(R.string.generic_error)
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.padding_md))
    ) {
        Text(
            text = errorMessage,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            modifier = modifier
                .padding(dimensionResource(R.dimen.padding_md))
                .align(Alignment.Center)
        )
    }

}

@Preview
@Composable
private fun ErrorScreenPrev() {
    ErrorScreen(
        error = UiState.Error(messageStringRes = R.string.generic_error),
        modifier = Modifier
    )
}
