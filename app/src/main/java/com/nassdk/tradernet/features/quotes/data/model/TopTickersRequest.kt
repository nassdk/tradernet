package com.nassdk.tradernet.features.quotes.data.model

import kotlinx.serialization.Serializable

@Serializable
data class TopTickersRequest(
    val cmd: String = "getTopSecurities",
    val params: TopTickersParams = TopTickersParams()
)

@Serializable
data class TopTickersParams(
    val type: String = "stocks",
    val exchange: String = "europe",
    val gainers: Int = 1,
    val limit: Int = 10
)
