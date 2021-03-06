package me.philoproject.starwarsseeker.viewmodels

import androidx.lifecycle.ViewModel
import io.realm.Realm
import me.philoproject.starwarsseeker.remote.models.realm.PlanetModel
import me.philoproject.starwarsseeker.remote.models.realm.RealmViewModel
import me.philoproject.starwarsseeker.remote.models.realm.findPlanetByName

/**
 * ViewModel responsible for binding the Planet DTO info to the View
 */
class PlanetViewModel : ViewModel(), RealmViewModel<PlanetModel> {

    var name: String = ""
    var climate: String = ""
    var terrain: String = ""
    var diameter: String = ""
    var population: String = ""

    private var realm: Realm? = null

    init {
        realm = Realm.getDefaultInstance()
    }

    /**
     * Retrieves the Planet from realm and maps into this view model
     */
    fun loadPlanetFromRealm(name: String) {
        realm?.findPlanetByName(name)?.let {
            fromRealmModel(it)
        }
    }

    /**
     * Map the RealmModel DTO to this ViewModel
     */
    override fun fromRealmModel(realmModel: PlanetModel) {
        name = realmModel.name
        climate = realmModel.climate.capitalizeFirstCharacter()
        terrain = realmModel.terrain.capitalizeFirstCharacter()
        diameter = realmModel.diameter.capitalizeFirstCharacter()
        population = realmModel.population.capitalizeFirstCharacter()
    }

    override fun onCleared() {
        super.onCleared()
        realm?.close()
        realm = null
    }
}