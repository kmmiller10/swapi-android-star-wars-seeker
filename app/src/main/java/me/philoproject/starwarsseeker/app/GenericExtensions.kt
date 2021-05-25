package me.philoproject.starwarsseeker.app

import java.lang.Exception

fun Any?.nonNullString(): String {
    if(this == null) return ""

    return try {
        (this as? String) ?: ""
    } catch (e: Exception) {
        ""
    }
}