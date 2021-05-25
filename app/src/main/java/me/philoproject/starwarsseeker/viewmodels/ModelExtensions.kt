package me.philoproject.starwarsseeker.viewmodels

import me.philoproject.starwarsseeker.remote.models.realm.CharacterModel
import java.util.*

/**
 * Sorts a list of CharacterModels by name using lowercase comparisons since upper and lower case
 * have different values in the ASCII character set
 */
fun List<CharacterModel>.sortModels(): List<CharacterModel> {
    return sortedBy { it.name.lowercase() }
}

/**
 * Capitalizes the first character of Star Wars Model detail fields given in lowercase by the API
 *
 * @param ignoreNotApplicable - Some fields are given as "n/a" and it may be preferred to keep this is in lower case
 * @return The string with the first letter capitalized, unless it was "n/a" and specified to ignore
 */
fun String.capitalizeFirstCharacter(ignoreNotApplicable: Boolean = true): String {
    if(ignoreNotApplicable && equals("n/a", ignoreCase = true)) return this

    return replaceFirstChar {
        if(it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
    }
}