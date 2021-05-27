package me.philoproject.starwarsseeker.remote.base

import me.philoproject.starwarsseeker.remote.base.AppException.*

/**
 * Parses HTTP error codes and throwables into usable exceptions that can be handled by this app
 */
interface AppExceptionParser {
    /**
     * Parses HTTP status codes that this app wants to handle, or a generic exception if it is unrecognized
     *
     * @param httpStatusCode - The HTTP error code
     * @return A parsed AppException that is recognized and handled by this app
     */
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

    /**
     * Parses a throwable from a coroutine network call. This can happen if the device is offline or if there
     * is another error that prevents the call from being made
     *
     * @param throwable - The coroutine exception that was thrown
     * @return A parsed AppException that is recognized and handled by this app
     */
    fun parseThrowable(throwable: Throwable): AppException {
        return when(throwable) {
            is java.net.UnknownHostException -> OFFLINE_EXCEPTION
            else -> GENERIC_EXCEPTION
        }
    }
}