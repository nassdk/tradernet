package com.nassdk.tradernet.features.quotes.domain.entity

import java.math.BigDecimal

data class QuoteEntity(
    val name: String,
    val ticker: String,
    val change: BigDecimal?,
    val lastDealStock: String?,
    val lastDealPrice: BigDecimal?,
    val changePercent: BigDecimal?,
    val prevChangePercent: BigDecimal?,
    val minStep: BigDecimal?,
) {
    val logoUrl: String
        get() = "https://tradernet.com/logos/get-logo-by-ticker?ticker=${ticker.lowercase()}"
}