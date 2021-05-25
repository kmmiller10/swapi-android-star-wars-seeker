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

class CharacterViewModel(private val planetRepo: PlanetRepo) : ViewModel(), RealmViewModel<CharacterModel> {

    var name: String = ""
    var height: String = ""
    var mass: String = ""
    var birthYear: String = ""
    var hairColor: String = ""
        get() = field.capitalizeFirstCharacter()
    var skinColor: String = ""
        get() = field.capitalizeFirstCharacter()
    var eyeColor: String = ""
        get() = field.capitalizeFirstCharacter()
    var gender: String = ""
        get() = field.capitalizeFirstCharacter()
    private var planetUrl: String = ""

    private val mPlanetName = MutableLiveData<String>()
    val planetName: LiveData<String> get() = mPlanetName

    fun fetchPlanet() {
        if(planetUrl.isEmpty()) return

        // First search for the planet in realm for a quick load
        val cachedPlanetName = findCachedPlanet(planetUrl)?.name.nonNullString()
        mPlanetName.postValue(cachedPlanetName)

        // Then call the API to fetch updated details
        viewModelScope.launch {
            val response = planetRepo.getPlanet(planetUrl)
            if(response.status == Status.Success) {
                response.data?.let { planetModel ->
                    planetModel.savePlanetToRealm()
                    mPlanetName.postValue(planetModel.name)
                }
            }
        }
    }

    private fun findCachedPlanet(url: String): PlanetModel? {
        Realm.getDefaultInstance().use { rlm ->
            return rlm.findPlanetByUrl(url)
        }
    }

    override fun fromRealmModel(realmModel: CharacterModel) {
        name = realmModel.name
        height = realmModel.height
        mass = realmModel.mass
        birthYear = realmModel.birthYear
        hairColor = realmModel.hairColor
        skinColor = realmModel.skinColor
        eyeColor = realmModel.eyeColor
        gender = realmModel.gender
        planetUrl = realmModel.homeWorld
    }
}