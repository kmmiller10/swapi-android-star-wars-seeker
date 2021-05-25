package me.philoproject.starwarsseeker.remote.api.planet

import me.philoproject.starwarsseeker.remote.base.Resource
import me.philoproject.starwarsseeker.remote.models.realm.PlanetModel

interface PlanetRepo {
    suspend fun getPlanet(planetUrl: String): Resource<PlanetModel>
}