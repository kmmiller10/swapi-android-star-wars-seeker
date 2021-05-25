package me.philoproject.starwarsseeker.viewmodels

import androidx.lifecycle.ViewModel
import me.philoproject.starwarsseeker.remote.models.realm.PlanetModel
import me.philoproject.starwarsseeker.remote.models.realm.RealmViewModel

class PlanetViewModel : ViewModel(), RealmViewModel<PlanetModel> {

    var name: String = ""
    var climate: String = ""
        get() = field.capitalizeFirstCharacter()
    var terrain: String = ""
        get() = field.capitalizeFirstCharacter()
    var diameter: String = ""
    var population: String = ""

    override fun fromRealmModel(realmModel: PlanetModel) {
        name = realmModel.name
        climate = realmModel.climate
        terrain = realmModel.terrain
        diameter = realmModel.diameter
        population = realmModel.population
    }
}