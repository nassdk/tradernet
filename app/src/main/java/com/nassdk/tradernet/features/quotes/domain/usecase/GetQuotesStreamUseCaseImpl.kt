package com.nassdk.tradernet.features.quotes.domain.usecase

import com.nassdk.tradernet.features.quotes.domain.entity.QuoteEntity
import com.nassdk.tradernet.features.quotes.domain.repo.QuotesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

internal class GetQuotesStreamUseCaseImpl(
    private val repository: QuotesRepository,
) : GetQuotesStreamUseCase {

    override fun invoke(): Flow<List<QuoteEntity>> = flow {
        val tickers = repository.getTopTickers()
        emitAll(repository.collectQuotes(tickers))
    }
}