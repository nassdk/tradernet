package com.nassdk.tradernet.features.quotes.di

import com.nassdk.tradernet.core.config.NetworkConfig
import com.nassdk.tradernet.core.dispatchers.DefaultDispatchersProvider
import com.nassdk.tradernet.core.dispatchers.DispatchersProvider
import com.nassdk.tradernet.core.formatter.AmountFormatter
import com.nassdk.tradernet.core.logger.AppLogger
import com.nassdk.tradernet.features.quotes.data.client.WebSocketClient
import com.nassdk.tradernet.features.quotes.data.repo.QuotesRepositoryImpl
import com.nassdk.tradernet.features.quotes.domain.repo.QuotesRepository
import com.nassdk.tradernet.features.quotes.domain.usecase.GetQuotesStreamUseCase
import com.nassdk.tradernet.features.quotes.domain.usecase.GetQuotesStreamUseCaseImpl
import com.nassdk.tradernet.features.quotes.presentation.mapper.QuotesUiMapper
import com.nassdk.tradernet.features.quotes.presentation.viewmodel.QuotesViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

internal object QuotesComponent {

    private val dispatchersProvider: DispatchersProvider by lazy {
        DefaultDispatchersProvider()
    }

    private val json: Json by lazy {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
        }
    }

    private val httpClient: HttpClient by lazy {
        HttpClient(OkHttp) {
            defaultRequest {
                url(NetworkConfig.BASE_URL)
                header(HttpHeaders.ContentType, ContentType.Application.Json)
            }
            install(ContentNegotiation) {
                json(json)
            }
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        AppLogger.d("HttpClient", message)
                    }
                }
                level = LogLevel.ALL
            }
        }
    }

    private val webSocketClient: WebSocketClient by lazy {
        WebSocketClient(
            json = json,
            dispatchersProvider = dispatchersProvider,
        )
    }

    fun createQuotesViewModel(): QuotesViewModel {
        return QuotesViewModel(
            getQuotesStreamUseCase = createQuotesStreamUseCase(),
            quotesUiMapper = QuotesUiMapper(amountFormatter = AmountFormatter()),
        )
    }

    private fun createQuotesStreamUseCase(): GetQuotesStreamUseCase {
        return GetQuotesStreamUseCaseImpl(repository = createQuotesRepository())
    }

    private fun createQuotesRepository(): QuotesRepository {
        return QuotesRepositoryImpl(
            httpClient = httpClient,
            socketClient = webSocketClient,
        )
    }
}