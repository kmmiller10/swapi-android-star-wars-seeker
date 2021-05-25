package me.philoproject.starwarsseeker.remote.models.realm

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class CharacterModel : RealmObject() {
    @PrimaryKey
    @SerializedName("name")
    var name: String = ""

    @SerializedName("height")
    var height: String = ""

    @SerializedName("mass")
    var mass: String = ""

    @SerializedName("hair_color")
    var hairColor: String = ""

    @SerializedName("skin_color")
    var skinColor: String = ""

    @SerializedName("eye_color")
    var eyeColor: String = ""

    @SerializedName("birth_year")
    var birthYear: String = ""

    @SerializedName("gender")
    var gender: String = ""

    @SerializedName("homeworld")
    var homeWorld: String = ""

    fun equalsOther(other: CharacterModel): Boolean {
        return name == other.name
                && height == other.height
                && mass == other.mass
                && birthYear == other.birthYear
                && hairColor == other.hairColor
                && skinColor == other.skinColor
                && eyeColor == other.eyeColor
                && gender == other.gender
                && homeWorld == other.homeWorld
    }
}