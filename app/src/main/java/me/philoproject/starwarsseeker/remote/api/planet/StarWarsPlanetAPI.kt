package me.philoproject.starwarsseeker.remote.api.planet

import me.philoproject.starwarsseeker.remote.models.realm.PlanetModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface StarWarsPlanetAPI {
    @GET
    suspend fun getPlanet(@Url planetUrl: String) : Response<PlanetModel>
}