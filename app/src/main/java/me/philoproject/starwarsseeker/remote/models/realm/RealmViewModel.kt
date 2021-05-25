package me.philoproject.starwarsseeker.remote.models.realm

interface RealmViewModel<in T> {
    fun fromRealmModel(realmModel: T)
}