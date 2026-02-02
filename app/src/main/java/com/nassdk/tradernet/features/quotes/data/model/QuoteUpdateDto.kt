package com.nassdk.tradernet.features.quotes.data.model

import com.nassdk.tradernet.features.quotes.data.serializer.BigDecimalAsString
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuoteUpdateDto(
    @SerialName("c") val ticker: String? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("ltr") val lastDealStock: String? = null,
    @SerialName("chg") val change: BigDecimalAsString? = null,
    @SerialName("ltp") val lastDealPrice: BigDecimalAsString? = null,
    @SerialName("pcp") val changePercent: BigDecimalAsString? = null,
    @SerialName("min_step") val minStep: BigDecimalAsString? = null,
)
