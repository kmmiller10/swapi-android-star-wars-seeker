package me.philoproject.starwarsseeker.app

import java.lang.Exception

/**
 * Takes in Any type of object, including null, and returns a non-null String regardless of type
 *
 * @return The result of parsing this object as a String, or an empty string if it is null or another object type
 */
fun Any?.nonNullString(): String {
    if(this == null) return ""

    return try {
        (this as? String) ?: ""
    } catch (e: Exception) {
        ""
    }
}