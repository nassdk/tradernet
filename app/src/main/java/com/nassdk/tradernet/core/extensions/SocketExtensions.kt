package com.nassdk.tradernet.core.extensions

import com.nassdk.tradernet.core.logger.AppLogger
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.retryWhen

private const val MAX_ATTEMPTS = 5
private const val INITIAL_DELAY_MS = 1000L

internal fun <T> Flow<T>.retryWithExponentialBackoff(
    maxAttempts: Int = MAX_ATTEMPTS,
    initialDelayMs: Long = INITIAL_DELAY_MS,
): Flow<T> = retryWhen { cause, attempt ->
    if (attempt >= maxAttempts) {
        AppLogger.e("Websocket", "Max retry attempts ($maxAttempts) reached, giving up", cause)
        false
    } else {
        val delayMs = initialDelayMs * (1 shl attempt.toInt())
        AppLogger.w(
            "Websocket",
            "Connection lost (attempt ${attempt + 1}/$maxAttempts), retrying in ${delayMs}ms..."
        )
        delay(delayMs)
        true
    }
}
