package me.philoproject.starwarsseeker.remote.models

import com.google.gson.annotations.SerializedName

/**
 * DTO for the response model returned by the Star Wars API
 */
data class ListResponseModel<T>(
    @SerializedName("count") var count: Int,
    @SerializedName("next") var next: String,
    @SerializedName("previous") var previous: String,
    @SerializedName("results") var results: List<T>
)