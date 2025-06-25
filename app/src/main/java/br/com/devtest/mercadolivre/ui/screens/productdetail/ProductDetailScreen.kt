package br.com.devtest.mercadolivre.ui.screens.productdetail

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.devtest.mercadolivre.R
import br.com.devtest.mercadolivre.ui.commons.components.ErrorScreen
import br.com.devtest.mercadolivre.ui.commons.components.FreeShippingBadge
import br.com.devtest.mercadolivre.ui.commons.components.ImageCarousel
import br.com.devtest.mercadolivre.ui.models.ProductDetailUiModel
import br.com.devtest.mercadolivre.ui.state.UiState
import br.com.devtest.mercadolivre.ui.theme.MercadoLivreDevTestTheme
import br.com.devtest.mercadolivre.ui.viewmodels.ProductDetailViewModel
import br.com.devtest.mercadolivre.utils.AppLog
import br.com.devtest.mercadolivre.utils.toCurrencyString

@Composable
fun ProductDetailScreen(
    viewModel: ProductDetailViewModel,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(
        key1 = Unit,
    ) {
        viewModel.fetchProductDetail()
    }

    when (state.value) {
        is UiState.Error -> {
            val error = (state.value as UiState.Error)
            if (error.code != 404) {
                ErrorScreen(
                    errorMessage = error.message,
                    modifier = modifier
                        .fillMaxSize()
                        .padding(dimensionResource(R.dimen.padding_md))
                )
            } else {
                Toast.makeText(
                    context,
                    stringResource(R.string.product_details_not_found),
                    Toast.LENGTH_LONG
                ).show()
                navigateBack()
            }
        }

        is UiState.Loading -> {
            Box(
                modifier = modifier
                    .fillMaxSize()
            ){
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier
                        .size(64.dp)
                        .align(Alignment.Center)
                )
            }
        }

        is UiState.Success -> {
            val productDetailUiModel =
                (state.value as UiState.Success<ProductDetailUiModel>).data

            ProductContent(
                productDetailUiModel = productDetailUiModel,
                modifier = modifier
            )
        }
    }
}

@Composable
fun ProductContent(
    productDetailUiModel: ProductDetailUiModel,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current.density
    val widthPx = LocalWindowInfo.current.containerSize.width
    val width = (widthPx / density).dp

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        ImageCarousel(
            imageUrls = productDetailUiModel.images,
            modifier = Modifier
                .fillMaxWidth()
                .height(width),
            placeholder = painterResource(R.drawable.loading_image_placeholder),
            contentScale = ContentScale.Fit,
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            error = painterResource(R.drawable.no_image_placeholder),
            //The height size of the image should be the same as the width to maintain aspect ratio
            imageSize = width,
            onError = {
                AppLog.e(
                    "ProductDetailScreen",
                    "Error loading image: ${it.result.throwable.message}"
                )
            }
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_lg)))

        Text(
            text = productDetailUiModel.title,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .padding(horizontal = dimensionResource(R.dimen.padding_md))
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_xs)))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = dimensionResource(R.dimen.padding_md),
                    vertical = dimensionResource(R.dimen.padding_xxs)
                )
        ) {
            val brandAttribute = productDetailUiModel.attributes.firstOrNull { it.type == "BRAND" }
            val brand = if (brandAttribute != null && !brandAttribute.value.isNullOrEmpty()) {
                stringResource(R.string.brand_name, brandAttribute.value)
            } else {
                stringResource(R.string.brand_name, stringResource(R.string.generic_brand))
            }

            Text(
                text = brand,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .weight(0.5f)
            )

            val colorAttribute = productDetailUiModel.attributes.firstOrNull { it.type == "COLOR" }
            if (colorAttribute != null && !colorAttribute.value.isNullOrEmpty()) {
                val strResource = stringResource(R.string.color_name, colorAttribute.value)
                Text(
                    text = strResource,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .weight(0.5f)
                )
            }
        }

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_xs)))

        Text(
            text = productDetailUiModel.price.toCurrencyString(productDetailUiModel.currencyId),
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier
                .padding(horizontal = dimensionResource(R.dimen.padding_md))
        )

        if (productDetailUiModel.freeShipping) {
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_xs)))
            FreeShippingBadge(
                textStyle = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(horizontal = dimensionResource(R.dimen.padding_md))
            )
        }
    }
}

@Preview
@Composable
fun ProductContentPrev() {
    MercadoLivreDevTestTheme {
        ProductContent(
            productDetailUiModel = ProductDetailUiModel(
                id = "12345",
                title = "Produto de Teste",
                price = "100.00".toBigDecimal(),
                images = listOf(
                    "https://example.com/image1.jpg",
                    "https://example.com/image2.jpg"
                ),
                freeShipping = true,
                originalPrice = "120.00".toBigDecimal(),
                description = "Descrição do produto de teste"
            )
        )
    }
}