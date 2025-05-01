package com.example.catsapp.core.network.util

import kotlinx.coroutines.delay
import retrofit2.HttpException
import java.io.IOException
import kotlin.math.pow
import kotlin.random.Random

suspend fun <T> retryIO(
    maxRetries: Int = 3,
    initialDelay: Long = 1_000L,
    factor: Double = 2.0,
    block: suspend () -> T
): T {
    var currentDelay = initialDelay
    var attempt = 0

    while (true) {
        try {
            // 실제 네트워크 호출
            return block()
        } catch (e: Exception) {
            // 재시도 여부 결정:
            // 1) HttpException(HTTP 429, 503, 5xx)
            // 2) IOException (네트워크/타임아웃 등)
            val shouldRetry = when (e) {
                is HttpException -> {
                    val code = e.code()
                    code == 429 || code == 503 || code >= 500
                }
                is IOException -> true
                else -> false
            }

            attempt++
            if (!shouldRetry || attempt >= maxRetries) {
                throw e
            }

            val jitter = Random.nextLong(currentDelay / 2)
            delay(currentDelay + jitter)

            currentDelay = (initialDelay * factor.pow(attempt)).toLong()
        }
    }
}