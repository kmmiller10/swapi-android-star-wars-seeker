package me.philoproject.starwarsseeker.remote.api.character

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.philoproject.starwarsseeker.remote.base.AppExceptionParser
import me.philoproject.starwarsseeker.remote.base.Resource
import me.philoproject.starwarsseeker.remote.models.ListResponseModel
import me.philoproject.starwarsseeker.remote.models.realm.CharacterModel

class CharacterRepoImpl(private val api: StarWarsCharacterAPI) : CharacterRepo, AppExceptionParser {

    override suspend fun getCharacterList(query: String): Resource<ListResponseModel<CharacterModel>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getCharacterList(query)

                if(response.isSuccessful) {
                    // Successful response. Check if the result is empty or not
                    val data = response.body()
                    if(data?.results?.isEmpty() == true) {
                        Resource.empty()
                    } else {
                        Resource.success(data)
                    }

                } else {
                    // Handle the error code by parsing into a local AppException
                    Resource.error(parseErrorCode(response.code()))
                }
            } catch(throwable: Throwable) {
                // This is usually caught when the device is offline
                Resource.error(parseThrowable(throwable))
            }
        }
    }
}