package me.philoproject.starwarsseeker.remote.api.planet

import me.philoproject.starwarsseeker.remote.base.Resource
import me.philoproject.starwarsseeker.remote.models.realm.PlanetModel

/**
 * Defines the network calls and return types for the Planet APIs handled by this app
 */
interface PlanetRepo {
    suspend fun getPlanet(planetUrl: String): Resource<PlanetModel>
}