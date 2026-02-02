package com.nassdk.tradernet.features.quotes.presentation.mapper

import com.nassdk.tradernet.core.formatter.AmountFormatter
import com.nassdk.tradernet.features.quotes.domain.entity.QuoteEntity
import com.nassdk.tradernet.features.quotes.presentation.model.PriceChangeDirection
import com.nassdk.tradernet.features.quotes.presentation.model.QuoteColor
import com.nassdk.tradernet.features.quotes.presentation.model.QuoteUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import java.math.BigDecimal

internal class QuotesUiMapper(private val amountFormatter: AmountFormatter) {
    fun map(quotes: List<QuoteEntity>): ImmutableList<QuoteUiModel> {
        return quotes.map { entity ->
            QuoteUiModel(
                uiKey = entity.ticker,
                title = entity.ticker,
                logoUrl = entity.logoUrl,
                subtitle = entity.buildSubtitle(),
                trailingSubtitle = entity.buildTrailingSubtitle(),
                trailingTitleColor = entity.getTrailingTitleColor(),
                priceChangeDirection = entity.getPriceChangeDirection(),
                trailingTitle = amountFormatter.formatPercent(entity.changePercent),
            )
        }.toImmutableList()
    }

    private fun QuoteEntity.buildTrailingSubtitle(): String {
        return buildString {
            append(amountFormatter.format(lastDealPrice, minStep))
            if (change != null) {
                append(" (")
                append(amountFormatter.formatWithSign(change, minStep))
                append(")")
            }
        }
    }

    private fun QuoteEntity.buildSubtitle(): String? {
        return if (!lastDealStock.isNullOrBlank() && name.isNotBlank()) {
            buildString {
                append(lastDealStock)
                if (name.isNotBlank()) append(" | $name")
            }
        } else {
            null
        }
    }

    private fun QuoteEntity.getTrailingTitleColor(): QuoteColor {
        return when {
            changePercent == null -> QuoteColor.NEUTRAL
            changePercent > BigDecimal.ZERO -> QuoteColor.POSITIVE
            changePercent < BigDecimal.ZERO -> QuoteColor.NEGATIVE
            else -> QuoteColor.NEUTRAL
        }
    }

    private fun QuoteEntity.getPriceChangeDirection(): PriceChangeDirection {
        return when {
            prevChangePercent == null || changePercent == null -> PriceChangeDirection.NONE
            changePercent > prevChangePercent -> PriceChangeDirection.UP
            changePercent < prevChangePercent -> PriceChangeDirection.DOWN
            else -> PriceChangeDirection.NONE
        }
    }
}