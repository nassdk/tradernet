package com.nassdk.tradernet.features.quotes.data.repo

import com.nassdk.tradernet.core.logger.AppLogger
import com.nassdk.tradernet.features.quotes.data.client.WebSocketClient
import com.nassdk.tradernet.features.quotes.data.model.TopTickersRequest
import com.nassdk.tradernet.features.quotes.data.model.TopTickersResponse
import com.nassdk.tradernet.features.quotes.domain.entity.QuoteEntity
import com.nassdk.tradernet.features.quotes.domain.repo.QuotesRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow

internal class QuotesRepositoryImpl(
    private val httpClient: HttpClient,
    private val socketClient: WebSocketClient,
) : QuotesRepository {

    @Volatile
    private var cachedTickers: List<String>? = null

    private val fallbackTickers = listOf(
        "SP500.IDX", "AAPL.US", "RSTI", "GAZP", "MRKZ", "RUAL", "HYDR", "MRKS", "SBER", "FEES",
        "TGKA", "VTBR", "ANH.US", "VICL.US", "BURG.US", "NBL.US", "YETI.US", "WSFS.US", "NIO.US",
        "DXC.US", "MIC.US", "HSBC.US", "EXPN.EU", "GSK.EU", "SHP.EU", "MAN.EU", "DB1.EU", "MUV2.EU",
        "TATE.EU", "KGF.EU", "MGGT.EU", "SGGD.EU", "AI.US"
    )

    override fun collectQuotes(tickers: List<String>): Flow<List<QuoteEntity>> {
        return socketClient.getQuotesFlow(tickers)
    }

    override suspend fun getTopTickers(): List<String> {
        cachedTickers?.let { return it }

        return try {
            httpClient.post("tradernet-api/quotes-get-top-securities") {
                setBody(TopTickersRequest())
            }.body<TopTickersResponse>().tickers.also {
                cachedTickers = it
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            AppLogger.e("QuotesRepository", "Error getting top tickers", e)
            fallbackTickers.also { cachedTickers = it }
        }
    }
}