package me.philoproject.starwarsseeker.remote.api.character

import me.philoproject.starwarsseeker.remote.base.Resource
import me.philoproject.starwarsseeker.remote.models.ListResponseModel
import me.philoproject.starwarsseeker.remote.models.realm.CharacterModel

interface CharacterRepo {
    suspend fun getCharacterList(query: String): Resource<ListResponseModel<CharacterModel>>
}