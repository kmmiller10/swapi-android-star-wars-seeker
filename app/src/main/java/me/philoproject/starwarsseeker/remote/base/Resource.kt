package me.philoproject.starwarsseeker.remote.base

/**
 * Wraps the result of a network call, including the DTO and a Status to track states and exceptions
 */
class Resource<out T> private constructor(val status: Status, val data: T?) {
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.Success, data)
        }

        fun <T> error(appError: AppException?): Resource<T> {
            return Resource(Status.Error(appError), null)
        }

        fun <T> empty(): Resource<T> {
            return Resource(Status.Empty, null)
        }
    }
}