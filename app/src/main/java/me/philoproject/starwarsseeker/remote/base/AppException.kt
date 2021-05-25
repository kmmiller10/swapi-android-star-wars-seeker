package me.philoproject.starwarsseeker.remote.base

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
