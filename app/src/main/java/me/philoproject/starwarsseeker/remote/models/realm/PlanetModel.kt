package me.philoproject.starwarsseeker.remote.models.realm

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * DTO for a Star Wars Planet. It serializes the JSON from the SWAPI response and gets cached in the Realm instance
 */
open class PlanetModel : RealmObject() {
    @PrimaryKey
    @SerializedName("name")
    var name: String = ""

    @SerializedName("climate")
    var climate: String = ""

    @SerializedName("terrain")
    var terrain: String = ""

    @SerializedName("diameter")
    var diameter: String = ""

    @SerializedName("population")
    var population: String = ""

    // Planet URL appears unused, but its actually used in a Realm query to see if this planet has been cached before.
    // Realm finds properties by string, so this property isn't referenced directly
    @SerializedName("url")
    var url: String = ""
}