package br.com.devtest.mercadolivre.ui.commons.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.devtest.mercadolivre.ui.theme.DarkGray
import br.com.devtest.mercadolivre.ui.theme.LightGray
import br.com.devtest.mercadolivre.ui.theme.MercadoLivreDevTestTheme
import br.com.devtest.mercadolivre.ui.theme.TertiaryBlue

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    query: String = "",
    onQueryChange: (String) -> Unit = {},
    onSearch: () -> Unit = {},
    placeholder: String? = null
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        shape = RoundedCornerShape(16.dp),
        leadingIcon = {
            Icon(Icons.Filled.Search, contentDescription = null, tint = TertiaryBlue)
        },
        singleLine = true,
        placeholder = if (placeholder != null) {
            {
                Text(text = placeholder, maxLines = 1)
            }
        } else null,
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            disabledContainerColor = MaterialTheme.colorScheme.surface,
            focusedTextColor = MaterialTheme.colorScheme.onBackground,
            unfocusedTextColor = DarkGray,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledLeadingIconColor = DarkGray,
            disabledTextColor = LightGray
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearch()
            }
        ),
        modifier = modifier
            .fillMaxWidth()
    )
}

@Preview
@Composable
private fun SearchBarPreview() {
    MercadoLivreDevTestTheme(dynamicColor = false) {
        SearchBar(
            query = "",
            onQueryChange = {},
            placeholder = "",
            modifier = Modifier.fillMaxWidth()
        )
    }
}