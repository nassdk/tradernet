package com.nassdk.tradernet.features.quotes.domain.usecase

import com.nassdk.tradernet.features.quotes.domain.entity.QuoteEntity
import kotlinx.coroutines.flow.Flow

interface GetQuotesStreamUseCase {
    fun invoke(): Flow<List<QuoteEntity>>
}