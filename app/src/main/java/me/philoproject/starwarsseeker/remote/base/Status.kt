package me.philoproject.starwarsseeker.remote.base

/**
 * Represents the different states of a network call and tracks errors as parsed exceptions that
 * can be handled by this app
 */
sealed class Status {
    object Success : Status()
    data class Error(val appError: AppException?) : Status()
    object Empty : Status()
    object Running : Status()
}