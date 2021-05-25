package me.philoproject.starwarsseeker.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.philoproject.starwarsseeker.remote.api.character.CharacterRepo
import me.philoproject.starwarsseeker.remote.base.Status
import me.philoproject.starwarsseeker.remote.models.realm.CharacterModel
import me.philoproject.starwarsseeker.remote.models.realm.saveCharactersToRealm

class CharacterListViewModel(private val repo: CharacterRepo) : ViewModel() {
    var lastSearch: String = ""

    private val mStatus = MutableLiveData<Status>()
    val status: LiveData<Status> get() = mStatus

    private val mCharacters = MutableLiveData<List<CharacterModel>>()
    val characters: LiveData<List<CharacterModel>> get() = mCharacters

    fun performSearch(query: String) {
        lastSearch = query

        mStatus.postValue(Status.Running)
        viewModelScope.launch {
            val response = repo.getCharacterList(query)
            when(response.status) {
                Status.Success -> {
                    response.data?.let { responseData ->
                        // Update the LiveData property for character UI view models
                        val models = responseData.results.sortModels()
                        mCharacters.postValue(models)

                        // Write or update these characters in realm so we can retrieve later before querying the API
                        models.saveCharactersToRealm()
                    }
                    mStatus.postValue(Status.Success)
                }
                Status.Empty -> {
                    mCharacters.postValue(listOf())
                    mStatus.postValue(Status.Empty)
                }
                Status.Running -> {
                    mStatus.postValue(Status.Running)
                }
                is Status.Error -> {
                    mStatus.postValue(response.status)
                }
            }
        }
    }

    fun restoreCharacters(characters: List<CharacterModel>) {
        mCharacters.postValue(characters)
    }
}