package com.nassdk.tradernet.features.quotes.presentation.ui

import com.nassdk.tradernet.features.quotes.presentation.model.PriceChangeDirection
import com.nassdk.tradernet.features.quotes.presentation.model.QuoteColor
import com.nassdk.tradernet.features.quotes.presentation.model.QuoteUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

internal object PreviewArguments {
    fun getPreviewQuotes(): ImmutableList<QuoteUiModel> {
        return persistentListOf(
            QuoteUiModel(
                uiKey = "1",
                title = "AAPL",
                logoUrl = "https://logo.clearbit.com/apple.com",
                subtitle = "Apple Inc.",
                trailingTitle = "$150.00",
                trailingSubtitle = "1.5%",
                trailingTitleColor = QuoteColor.POSITIVE,
                priceChangeDirection = PriceChangeDirection.UP,
            ),
            QuoteUiModel(
                uiKey = "2",
                title = "TSLA",
                logoUrl = "https://logo.clearbit.com/tesla.com",
                subtitle = "Tesla Inc.",
                trailingTitle = "$700.00",
                trailingSubtitle = "2.3%",
                trailingTitleColor = QuoteColor.NEGATIVE,
                priceChangeDirection = PriceChangeDirection.DOWN,
            ),
            QuoteUiModel(
                uiKey = "3",
                title = "AMZN",
                logoUrl = "https://logo.clearbit.com/amazon.com",
                subtitle = "Amazon.com Inc.",
                trailingTitle = "$3,000.00",
                trailingSubtitle = "0.00",
                trailingTitleColor = QuoteColor.NEUTRAL,
                priceChangeDirection = PriceChangeDirection.NONE,
            ),
        )
    }
}