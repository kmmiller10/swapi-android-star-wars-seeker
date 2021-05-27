package me.philoproject.starwarsseeker.remote.models.realm

/**
 * Indicates a ViewModel which represents a RealmModel DTO. Provides a method for mapping data
 * from the DTO to the ViewModel
 */
interface RealmViewModel<in T> {
    fun fromRealmModel(realmModel: T)
}