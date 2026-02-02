package com.nassdk.tradernet.features.quotes.data.client

import com.nassdk.tradernet.core.config.NetworkConfig
import com.nassdk.tradernet.core.dispatchers.DispatchersProvider
import com.nassdk.tradernet.core.extensions.retryWithExponentialBackoff
import com.nassdk.tradernet.core.logger.AppLogger
import com.nassdk.tradernet.features.quotes.data.model.QuoteUpdateDto
import com.nassdk.tradernet.features.quotes.domain.entity.QuoteEntity
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.add
import kotlinx.serialization.json.addJsonArray
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonPrimitive

internal class WebSocketClient(
    private val json: Json,
    private val dispatchersProvider: DispatchersProvider,
) {

    private val client = HttpClient(OkHttp) {
        install(WebSockets.Plugin)
    }

    fun getQuotesFlow(tickers: List<String>): Flow<List<QuoteEntity>> {
        val quotesMap = mutableMapOf<String, QuoteEntity>()
        return channelFlow {
            client.webSocket(NetworkConfig.WEBSOCKET_URL) {
                val subscribeMessage = buildJsonArray {
                    add("quotes")
                    addJsonArray {
                        tickers.forEach { add(it) }
                    }
                }.toString()

                AppLogger.d("WebSocket", "Subscribing to: $subscribeMessage")
                send(Frame.Text(subscribeMessage))

                for (frame in incoming) {
                    if (frame is Frame.Text) {
                        handleTextFrame(frame, quotesMap) { updatedList ->
                            send(updatedList)
                        }
                    }
                }
            }

            awaitClose {
                AppLogger.d("WebSocket", "Closing WebSocket connection")
                client.close()
            }
        }
            .retryWithExponentialBackoff()
            .flowOn(dispatchersProvider.io)
    }

    private suspend fun handleTextFrame(
        frame: Frame.Text,
        quotesMap: MutableMap<String, QuoteEntity>,
        onUpdate: suspend (List<QuoteEntity>) -> Unit,
    ) {
        try {
            val text = frame.readText()
            val jsonElement = json.parseToJsonElement(text)

            if (jsonElement is JsonArray && jsonElement.size >= 2) {
                val event = jsonElement[0].jsonPrimitive.content
                if (event == "q") {
                    processQuoteUpdate(jsonElement, quotesMap, onUpdate)
                }
            }
        } catch (e: Exception) {
            AppLogger.e("WebSocket", "Parse error", e)
        }
    }

    private suspend fun processQuoteUpdate(
        jsonElement: JsonArray,
        quotesMap: MutableMap<String, QuoteEntity>,
        onUpdate: suspend (List<QuoteEntity>) -> Unit,
    ) {
        val dataObject = jsonElement[1] as? JsonObject ?: return
        val update = json.decodeFromJsonElement<QuoteUpdateDto>(dataObject)
        val ticker = update.ticker ?: return

        val existing = quotesMap[ticker]
        val merged = mergeQuoteEntity(ticker, update, existing)

        if (existing != merged) {
            quotesMap[ticker] = merged
            onUpdate(quotesMap.values.toList())
        }
    }

    private fun mergeQuoteEntity(
        ticker: String,
        update: QuoteUpdateDto,
        existing: QuoteEntity?,
    ): QuoteEntity {
        return QuoteEntity(
            ticker = ticker,
            name = update.name ?: existing?.name.orEmpty(),
            change = update.change ?: existing?.change,
            prevChangePercent = existing?.changePercent,
            minStep = update.minStep ?: existing?.minStep,
            lastDealStock = update.lastDealStock ?: existing?.lastDealStock,
            lastDealPrice = update.lastDealPrice ?: existing?.lastDealPrice,
            changePercent = update.changePercent ?: existing?.changePercent,
        )
    }
}