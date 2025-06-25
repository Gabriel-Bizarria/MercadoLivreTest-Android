package br.com.devtest.mercadolivre.ui.commons.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import kotlinx.coroutines.launch

@Composable
fun ImageCarousel(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal,
    contentScale: ContentScale,
    imageSize: Dp,
    error: Painter? = null,
    placeholder: Painter? = null,
    onError: ((AsyncImagePainter.State.Error) -> Unit)? = null,
    imageUrls: List<String>
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .height(imageSize)
            .fillMaxWidth()
    ) {
        LazyRow(
            modifier = modifier,
            state = listState,
            horizontalArrangement = horizontalArrangement
        ) {
            items(imageUrls) { url ->

                AsyncImage(
                    model = url,
                    contentDescription = null,
                    contentScale = contentScale,
                    error = error,
                    onError = onError,
                    placeholder = placeholder,
                    modifier = Modifier
                        .size(imageSize)
                )
            }
        }


        // Left arrow
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Scroll Left",
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 8.dp)
                .size(32.dp)
                .clickable {
                    coroutineScope.launch {
                        val firstVisibleItem = listState.firstVisibleItemIndex
                        listState.animateScrollToItem((firstVisibleItem - 1).coerceAtLeast(0))
                    }
                },
            tint = Color.White
        )

        // Right arrow
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = "Scroll Right",
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 8.dp)
                .size(32.dp)
                .clickable {
                    coroutineScope.launch {
                        val nextIndex = listState.firstVisibleItemIndex + 1
                        listState.animateScrollToItem(nextIndex.coerceAtMost(imageUrls.lastIndex))
                    }
                },
            tint = Color.White
        )
    }
}