package com.nassdk.tradernet.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nassdk.tradernet.features.quotes.presentation.model.PriceChangeDirection
import com.nassdk.tradernet.ui.theme.QuoteNegative
import com.nassdk.tradernet.ui.theme.QuotePositive
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext


@Composable
internal fun PriceText(
    price: String,
    baseColor: Color,
    changeDirection: PriceChangeDirection,
    modifier: Modifier = Modifier,
) {
    val animatable = remember { Animatable(0f) }
    val previousDirection = remember { mutableStateOf(PriceChangeDirection.NONE) }

    val highlightColor = when (changeDirection) {
        PriceChangeDirection.UP -> QuotePositive
        PriceChangeDirection.DOWN -> QuoteNegative
        PriceChangeDirection.NONE -> Color.Transparent
    }

    LaunchedEffect(changeDirection) {
        if (changeDirection != PriceChangeDirection.NONE && changeDirection != previousDirection.value) {
            previousDirection.value = changeDirection
            try {
                animatable.snapTo(1f)
                delay(700)
                animatable.animateTo(0f, animationSpec = tween(300))
            } finally {
                withContext(NonCancellable) {
                    animatable.snapTo(0f)
                }
            }
        }
    }

    val progress = animatable.value
    val bgColor = lerp(Color.Transparent, highlightColor, progress)
    val textColor = if (progress > 0.01f) lerp(baseColor, Color.White, progress) else baseColor

    Box(
        modifier = modifier
            .background(color = bgColor, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 4.dp, vertical = 2.dp)
    ) {
        Text(
            text = price,
            fontSize = 20.sp,
            color = textColor,
            textAlign = TextAlign.End,
        )
    }
}
