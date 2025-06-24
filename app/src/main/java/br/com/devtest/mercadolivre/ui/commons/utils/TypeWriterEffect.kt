package br.com.devtest.mercadolivre.ui.commons.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay

@Composable
fun TypeWriterEffect(
    text: String,
    content: @Composable (String) -> Unit
) {
    var displayedText by remember { mutableStateOf("|") }
    var delayMillisBounce by remember { mutableLongStateOf(100L) }

    LaunchedEffect(text) {
        displayedText = ""
        for (char in text) {
            displayedText += char
            delayMillisBounce = calculateTypeWriterDelay()
            delay(delayMillisBounce)
        }
    }

    content(displayedText)
}

fun calculateTypeWriterDelay(
    minDelay: Long = 50L,
    maxDelay: Long = 150L
): Long {
    val randomDecimal = ((minDelay / 10)..(maxDelay / 10)).random() * 10L
    return randomDecimal
}