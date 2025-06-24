package br.com.devtest.mercadolivre.ui.screens.searchresult

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.devtest.mercadolivre.R
import br.com.devtest.mercadolivre.utils.toBigDecimalMonetary
import br.com.devtest.mercadolivre.utils.toCurrencyString
import br.com.devtest.mercadolivre.ui.models.ProductUiModel
import br.com.devtest.mercadolivre.ui.theme.AccentGreen
import br.com.devtest.mercadolivre.ui.theme.DarkGray
import br.com.devtest.mercadolivre.ui.theme.MercadoLivreDevTestTheme
import br.com.devtest.mercadolivre.ui.theme.lightGreen
import br.com.devtest.mercadolivre.utils.AppLog
import coil3.compose.AsyncImage

@Composable
fun SearchProductItem(
    productItem: ProductUiModel,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.background)
            .heightIn(max = 250.dp)
            .padding(8.dp)
    ) {
        AsyncImage(
            model = productItem.productImage,
            contentDescription = "Product Image",
            contentScale = ContentScale.Fit,
            onError = {
                AppLog.e(
                    "SearchProductItem",
                    "Error loading image: ${it.result.throwable.message} - ${productItem.productImage}"
                )
            },
            error = painterResource(R.drawable.no_image_placeholder),
            placeholder = painterResource(R.drawable.loading_image_placeholder),
            modifier = Modifier
                .weight(0.45f)
                .fillMaxSize()
                .padding(end = 8.dp)
                .align(Alignment.CenterVertically)
        )

        Column(
            modifier = Modifier
                .weight(0.45f)
                .fillMaxHeight()
        ) {
            Text(
                text = (productItem.brand ?: stringResource(R.string.generic_brand)).uppercase(),
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
            )

            Text(
                text = productItem.title,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_xs)))

            if (productItem.originalPrice != null) {
                Text(
                    text = productItem.originalPrice.toCurrencyString(productItem.currencyId),
                    textDecoration = TextDecoration.LineThrough,
                    color = DarkGray,
                    style = MaterialTheme.typography.labelSmall
                )
            }

            Text(
                text = productItem.price.toCurrencyString(productItem.currencyId),
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_sm)))

            if (productItem.isFreeShipping) {
                FreeShippingBadge()
            }
        }
    }
}

@Composable
private fun FreeShippingBadge(
    modifier: Modifier = Modifier
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
            style = MaterialTheme.typography.labelSmall,
            color = AccentGreen,
            modifier = Modifier.padding(
                vertical = 2.dp,
                horizontal = dimensionResource(R.dimen.padding_xs)
            )
        )
    }
}

@Preview
@Composable
private fun SearchProductItemPreview() {
    MercadoLivreDevTestTheme(dynamicColor = false) {
        SearchProductItem(
            productItem = ProductUiModel(
                id = "MLB123456789",
                title = "Sample Product Title",
                price = 19999.0.toBigDecimalMonetary(),
                isFreeShipping = true,
                originalPrice = 24999.0.toBigDecimalMonetary(),
                brand = "Sample Brand",
                currencyId = "BRL",
                productImage = "https://example.com/product_image.jpg"
            )
        )
    }
}