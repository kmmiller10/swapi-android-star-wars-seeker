package me.philoproject.starwarsseeker.remote.api.character

import me.philoproject.starwarsseeker.remote.base.Resource
import me.philoproject.starwarsseeker.remote.models.ListResponseModel
import me.philoproject.starwarsseeker.remote.models.realm.CharacterModel

/**
 * Defines the network calls and return types for the Character APIs handled by this app
 */
interface CharacterRepo {
    suspend fun getCharacterList(query: String): Resource<ListResponseModel<CharacterModel>>
}