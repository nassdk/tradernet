package com.nassdk.tradernet.features.quotes.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nassdk.tradernet.core.logger.AppLogger
import com.nassdk.tradernet.features.quotes.domain.usecase.GetQuotesStreamUseCase
import com.nassdk.tradernet.features.quotes.presentation.mapper.QuotesUiMapper
import com.nassdk.tradernet.features.quotes.presentation.model.QuoteUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

internal class QuotesViewModel(
    quotesUiMapper: QuotesUiMapper,
    getQuotesStreamUseCase: GetQuotesStreamUseCase,
) : ViewModel() {

    val quotes: StateFlow<ImmutableList<QuoteUiModel>> = getQuotesStreamUseCase.invoke()
        .map(quotesUiMapper::map)
        .catch { exception ->
            AppLogger.e("QuotesViewModel", "Error collecting quotes", exception)
            emit(persistentListOf()) // todo Тут бы обработку ошибок прикрутить. Но в ТЗ про это ничего
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = persistentListOf(),
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000L),
        )
}

