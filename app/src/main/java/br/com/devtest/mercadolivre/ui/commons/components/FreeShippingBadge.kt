package br.com.devtest.mercadolivre.ui.commons.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import br.com.devtest.mercadolivre.R
import br.com.devtest.mercadolivre.ui.theme.AccentGreen
import br.com.devtest.mercadolivre.ui.theme.lightGreen

@Composable
fun FreeShippingBadge(
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.labelSmall
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = lightGreen
        ),
        shape = RoundedCornerShape(2.dp),
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.free_shipping),
            style = textStyle,
            color = AccentGreen,
            modifier = Modifier.padding(
                vertical = 2.dp,
                horizontal = dimensionResource(R.dimen.padding_xs)
            )
        )
    }
}
