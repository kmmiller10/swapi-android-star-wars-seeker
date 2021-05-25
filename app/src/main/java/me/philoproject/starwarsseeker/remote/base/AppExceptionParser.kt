package me.philoproject.starwarsseeker.remote.base

import me.philoproject.starwarsseeker.remote.base.AppException.*

/**
 * Parses HTTP error codes and throwables into usable exceptions that can be handled by the app
 */
interface AppExceptionParser {
    fun parseErrorCode(httpStatusCode: Int): AppException {
        return when(httpStatusCode) {
            400 -> BAD_REQUEST_EXCEPTION
            404 -> NOT_FOUND_EXCEPTION
            408 -> TIMEOUT_EXCEPTION
            429 -> RATE_LIMIT_EXCEPTION
            500 -> SERVER_INTERNAL_EXCEPTION
            502 -> BAD_GATEWAY_EXCEPTION
            503 -> SERVICE_UNAVAILABLE_EXCEPTION
            504 -> GATEWAY_TIMEOUT_EXCEPTION
            else -> GENERIC_EXCEPTION
        }
    }

    fun parseThrowable(throwable: Throwable): AppException {
        return when(throwable) {
            is java.net.UnknownHostException -> OFFLINE_EXCEPTION
            else -> GENERIC_EXCEPTION
        }
    }
}