package me.philoproject.starwarsseeker.remote.models.realm

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

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

    @SerializedName("url")
    var url: String = ""
}