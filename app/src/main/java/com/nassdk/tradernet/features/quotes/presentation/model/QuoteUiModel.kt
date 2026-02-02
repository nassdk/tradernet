package com.nassdk.tradernet.features.quotes.presentation.model

internal data class QuoteUiModel(
    val uiKey: String,
    val title: String,
    val logoUrl: String,
    val subtitle: String?,
    val trailingTitle: String,
    val trailingSubtitle: String,
    val trailingTitleColor: QuoteColor,
    val priceChangeDirection: PriceChangeDirection,
)

enum class PriceChangeDirection {
    UP, DOWN, NONE
}