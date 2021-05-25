package me.philoproject.starwarsseeker.remote.api.planet

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.philoproject.starwarsseeker.remote.base.AppExceptionParser
import me.philoproject.starwarsseeker.remote.base.Resource
import me.philoproject.starwarsseeker.remote.models.realm.PlanetModel

class PlanetRepoImpl(private val api: StarWarsPlanetAPI) : PlanetRepo, AppExceptionParser {

    override suspend fun getPlanet(planetUrl: String): Resource<PlanetModel> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getPlanet(planetUrl)

                if(response.isSuccessful) {
                    Resource.success(response.body())
                } else {
                    // Parse the error code into a local AppException
                    Resource.error(parseErrorCode(response.code()))
                }
            } catch(throwable: Throwable) {
                // This is usually caught when the device is offline
                Resource.error(parseThrowable(throwable))
            }
        }
    }
}