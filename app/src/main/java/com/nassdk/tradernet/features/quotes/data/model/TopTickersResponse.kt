package com.nassdk.tradernet.features.quotes.data.model

import kotlinx.serialization.Serializable

@Serializable
internal data class TopTickersResponse(val tickers: List<String>)
