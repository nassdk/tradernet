package com.nassdk.tradernet.ui.modifiers

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun rememberShimmerBrush(
    targetValue: Float = 1000f,
    animationDuration: Int = 1000,
    shimmerColor: Color = Color.White.copy(alpha = 0.75f),
    backgroundColor: Color = Color.White.copy(alpha = 0.1f),
): Brush {
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer_transition")
    val translateAnimation = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = targetValue,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = animationDuration,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
    )

    return Brush.horizontalGradient(
        colors = listOf(
            shimmerColor,
            backgroundColor,
            shimmerColor,
            backgroundColor,
        ),
        startX = translateAnimation.value - targetValue,
        endX = translateAnimation.value + targetValue,
    )
}

@Composable
fun Modifier.shimmer(
    enabled: Boolean = true,
    targetValue: Float = 1000f,
    animationDuration: Int = 1000,
    shimmerColor: Color = Color.White.copy(alpha = 0.75f),
    backgroundColor: Color = Color.White.copy(alpha = 0.1f),
): Modifier = if (!enabled) {
    this
} else {
    this.then(
        Modifier.background(
            brush = rememberShimmerBrush(
                targetValue = targetValue,
                animationDuration = animationDuration,
                shimmerColor = shimmerColor,
                backgroundColor = backgroundColor
            )
        )
    )
}
