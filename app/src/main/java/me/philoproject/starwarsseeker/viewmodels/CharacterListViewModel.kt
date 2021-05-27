package me.philoproject.starwarsseeker.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.realm.Realm
import kotlinx.coroutines.launch
import me.philoproject.starwarsseeker.remote.api.character.CharacterRepo
import me.philoproject.starwarsseeker.remote.base.Status
import me.philoproject.starwarsseeker.remote.models.realm.CharacterModel
import me.philoproject.starwarsseeker.remote.models.realm.findCharactersByQuery
import me.philoproject.starwarsseeker.remote.models.realm.saveCharactersToRealm

/**
 * ViewModel responsible for performing the API call to retrieve the Character List, then bind
 * the DTO info to the View
 */
class CharacterListViewModel(private val repo: CharacterRepo) : ViewModel() {
    var lastSearch: String = ""

    private val mStatus = MutableLiveData<Status>()
    val status: LiveData<Status> get() = mStatus

    private val mCharacters = MutableLiveData<List<CharacterModel>>()
    val characters: LiveData<List<CharacterModel>> get() = mCharacters

    private var realm: Realm? = null

    init {
        realm = Realm.getDefaultInstance()
    }

    /**
     * Performs the API call to search for Characters that match this query, then updates LiveData properties
     * which are being observed by the fragment so it can update the UI
     */
    fun performSearch(query: String) {
        lastSearch = query

        mStatus.postValue(Status.Running)
        viewModelScope.launch {
            val response = repo.getCharacterList(query)
            when(response.status) {
                Status.Success -> {
                    response.data?.let { responseData ->
                        // Update the LiveData property for the Character models
                        val models = responseData.results.sortModels()
                        mCharacters.postValue(models)

                        // Write or update these Characters in realm so we can retrieve later before querying the API
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

    /**
     * Performs a local search on Characters in Realm
     *
     * @return The result of the query detached from Realm and sorted by name
     */
     fun queryRealmSorted(query: String): List<CharacterModel> {
        val rlm = realm ?: return listOf()
        val results = rlm.findCharactersByQuery(query)
        return rlm.copyFromRealm(results.sortModels())
    }

    /**
     * Restores search results on rotate or return to the list frag from the detail frag
     *
     * @return true if models were restored, false if the results are empty
     */
    fun restoreLastSearch(): Boolean {
        val models = queryRealmSorted(lastSearch)
        mCharacters.postValue(models)
        return models.isNotEmpty()
    }

    override fun onCleared() {
        super.onCleared()
        realm?.close()
        realm = null
    }
}