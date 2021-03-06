package me.philoproject.starwarsseeker.remote.models.realm

import io.realm.Case
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Finds all characters with a name that contains the search query, ignoring case
 *
 * @param query - The query to search by
 * @return All Characters that contain this query in their name
 */
fun Realm.findCharactersByQuery(query: String): RealmResults<CharacterModel> {
    return where(CharacterModel::class.java)
        .contains("name", query, Case.INSENSITIVE)
        .findAll()
}

/**
 * Finds all characters stored in realm
 *
 * @return All characters that have been searched and cached previously
 */
fun Realm.findAllCharacters(): RealmResults<CharacterModel> {
    return findCharactersByQuery("")
}

/**
 * Finds a Character by name, if it exists
 *
 * @param name - The name of the character
 * @return The cached Character, or null if not found
 */
fun Realm.findCharacterByName(name: String): CharacterModel? {
    return where(CharacterModel::class.java).equalTo("name", name).findFirst()
}

/**
 * Updates existing models or writes new models to Realm
 */
suspend fun List<CharacterModel>.saveCharactersToRealm() {
    withContext(Dispatchers.IO) {
        Realm.getDefaultInstance().use {
            it.executeTransaction { rlm ->
                rlm.copyToRealmOrUpdate(this@saveCharactersToRealm)
            }
        }
    }
}

/**
 * Finds a Planet by name, if it exists
 *
 * @param name - Name of the planet
 * @return The cached Planet or null if not found
 */
fun Realm.findPlanetByName(name: String): PlanetModel? {
    return where(PlanetModel::class.java).equalTo("name", name).findFirst()
}

/**
 * Finds a Planet by url, if it exists
 *
 * @param url - SWAPI URL corresponding to this planet
 * @return The cached Planet or null if not found
 */
fun Realm.findPlanetByUrl(url: String): PlanetModel? {
    return where(PlanetModel::class.java).equalTo("url", url).findFirst()
}

/**
 * Updates an existing planet model or writes it to realm
 */
suspend fun PlanetModel.savePlanetToRealm() {
    withContext(Dispatchers.IO) {
        Realm.getDefaultInstance().use {
            it.executeTransaction { rlm ->
                rlm.copyToRealmOrUpdate(this@savePlanetToRealm)
            }
        }
    }
}