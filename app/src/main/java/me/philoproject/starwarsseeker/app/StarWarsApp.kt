package me.philoproject.starwarsseeker.app

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration
import me.philoproject.starwarsseeker.app.modules.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class StarWarsApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // Start Koin (dependency injection)
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@StarWarsApp)
            modules(listOf(repositoryModule, retrofitModule, apiModule, viewModelModule))
        }

        // Set up Realm (local database)
        Realm.init(this)
        val realmConfig = RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build()
        Realm.setDefaultConfiguration(realmConfig)
    }
}