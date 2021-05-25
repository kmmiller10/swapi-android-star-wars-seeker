package me.philoproject.starwarsseeker.remote.api.character

import me.philoproject.starwarsseeker.remote.models.ListResponseModel
import me.philoproject.starwarsseeker.remote.models.realm.CharacterModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface StarWarsCharacterAPI {
    @GET("api/people/")
    suspend fun getCharacterList(@Query("search") search: String): Response<ListResponseModel<CharacterModel>>
}