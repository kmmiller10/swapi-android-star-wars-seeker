package me.philoproject.starwarsseeker.remote.base

/**
 * Represents HTTP exceptions or Throwables. Allows the app to parse error types and display user friendly
 * messages. If in the future the exception message/response message was desired to be logged or displayed,
 * this class could be changed to Sealed Class with the desired exception types containing an error message
 */
enum class AppException {
    // 400 range errors
    BAD_REQUEST_EXCEPTION,
    NOT_FOUND_EXCEPTION,
    TIMEOUT_EXCEPTION,
    RATE_LIMIT_EXCEPTION,

    // 500 range errors
    SERVER_INTERNAL_EXCEPTION,
    BAD_GATEWAY_EXCEPTION,
    SERVICE_UNAVAILABLE_EXCEPTION,
    GATEWAY_TIMEOUT_EXCEPTION,

    // Other
    GENERIC_EXCEPTION,
    OFFLINE_EXCEPTION
}
