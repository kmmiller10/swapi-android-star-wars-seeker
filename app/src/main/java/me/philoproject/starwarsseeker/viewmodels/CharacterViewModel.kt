package me.philoproject.starwarsseeker.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.realm.Realm
import kotlinx.coroutines.launch
import me.philoproject.starwarsseeker.app.nonNullString
import me.philoproject.starwarsseeker.remote.api.planet.PlanetRepo
import me.philoproject.starwarsseeker.remote.base.Status
import me.philoproject.starwarsseeker.remote.models.realm.*

/**
 * ViewModel responsible for binding the Character DTO info to the View. Must retrieve home world Planet
 * info, since it is given in the form of a URL
 */
class CharacterViewModel(private val planetRepo: PlanetRepo) : ViewModel(), RealmViewModel<CharacterModel> {

    var name: String = ""
    var height: String = ""
    var mass: String = ""
    var birthYear: String = ""
    var hairColor: String = ""
    var skinColor: String = ""
    var eyeColor: String = ""
    var gender: String = ""
    private var planetUrl: String = ""

    private val mPlanetName = MutableLiveData<String>()
    val planetName: LiveData<String> get() = mPlanetName

    private val mStatus = MutableLiveData<Status>()
    val status: LiveData<Status> get() = mStatus

    private var realm: Realm? = null

    init {
        realm = Realm.getDefaultInstance()
    }

    /**
     * Fetches the home world Planet for this Character and loads the name into the View Model
     */
    fun fetchPlanet() {
        if(planetUrl.isEmpty()) return

        // First search for the planet in realm for a quick load
        val cachedPlanetName = findCachedHomeWorld(planetUrl)?.name.nonNullString()
        mPlanetName.postValue(cachedPlanetName)

        // Then call the API to fetch updated details
        mStatus.postValue(Status.Running)
        viewModelScope.launch {
            val response = planetRepo.getPlanet(planetUrl)
            when(response.status) {
                Status.Success -> {
                    response.data?.let { planetModel ->
                        planetModel.savePlanetToRealm()
                        mPlanetName.postValue(planetModel.name)
                    }
                    mStatus.postValue(Status.Success)
                }
                Status.Empty -> {
                    // NO-OP for this status, since it is not a list
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
     * Searches Realm for this character's home world in case it is already cached and can be displayed immediately
     */
    private fun findCachedHomeWorld(url: String): PlanetModel? {
        return realm?.findPlanetByUrl(url)
    }

    /**
     * Retrieves the Character from realm and maps into this view model
     */
    fun loadCharacterFromRealm(name: String) {
        realm?.findCharacterByName(name)?.let {
            fromRealmModel(it)
        }
    }

    /**
     * Search realm for cached Characters. If there are no cached Characters, the frag will display an empty state
     */
    fun hasCachedCharacters(): Boolean {
        return realm?.findAllCharacters()?.isNotEmpty() ?: false
    }

    /**
     * Map the RealmModel DTO to this ViewModel
     */
    override fun fromRealmModel(realmModel: CharacterModel) {
        name = realmModel.name
        height = realmModel.height.capitalizeFirstCharacter()
        mass = realmModel.mass.capitalizeFirstCharacter()
        birthYear = realmModel.birthYear.capitalizeFirstCharacter()
        hairColor = realmModel.hairColor.capitalizeFirstCharacter()
        skinColor = realmModel.skinColor.capitalizeFirstCharacter()
        eyeColor = realmModel.eyeColor.capitalizeFirstCharacter()
        gender = realmModel.gender.capitalizeFirstCharacter()
        planetUrl = realmModel.homeWorldUrl
    }

    override fun onCleared() {
        super.onCleared()
        realm?.close()
        realm = null
    }
}