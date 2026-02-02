package com.nassdk.tradernet.features.quotes.domain.repo

import com.nassdk.tradernet.features.quotes.domain.entity.QuoteEntity
import kotlinx.coroutines.flow.Flow

internal interface QuotesRepository {
    fun collectQuotes(tickers: List<String>): Flow<List<QuoteEntity>>
    suspend fun getTopTickers(): List<String>
}