package me.philoproject.starwarsseeker.app.modules

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import me.philoproject.starwarsseeker.BuildConfig
import me.philoproject.starwarsseeker.remote.api.character.CharacterRepo
import me.philoproject.starwarsseeker.remote.api.character.CharacterRepoImpl
import me.philoproject.starwarsseeker.remote.api.character.StarWarsCharacterAPI
import me.philoproject.starwarsseeker.remote.api.planet.PlanetRepo
import me.philoproject.starwarsseeker.remote.api.planet.PlanetRepoImpl
import me.philoproject.starwarsseeker.remote.api.planet.StarWarsPlanetAPI
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val TIMEOUT_IN_SECONDS = 30L
const val BASE_URL = "https://swapi.dev/"

val repositoryModule = module {
    single<CharacterRepo> { CharacterRepoImpl(get()) }
    single<PlanetRepo> { PlanetRepoImpl(get()) }
}

val apiModule = module {
    single { provideCharacterApi(get()) }
    single { providePlanetApi(get()) }
}

val retrofitModule = module {
    factory { provideGson() }
    factory { provideHttpLoggingInterceptor() }
    factory { provideHttpClient(get()) }
    single { provideRetrofit(get(), get()) }
}

/* Provide API classes */
fun provideCharacterApi(retrofit: Retrofit): StarWarsCharacterAPI {
    return retrofit.create(StarWarsCharacterAPI::class.java)
}

fun providePlanetApi(retrofit: Retrofit): StarWarsPlanetAPI {
    return retrofit.create(StarWarsPlanetAPI::class.java)
}

/* Provide Gson factory */
fun provideGson(): Gson {
    return GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create()
}

/* Provide HTTP logging interceptor */
fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
    return HttpLoggingInterceptor().apply {
        if(BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
    }
}

/* Provide HTTP client for Retrofit */
fun provideHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
    return OkHttpClient.Builder()
        .connectTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
        .readTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS) // Writing to SWAPI is not allowed, so writeTimeout is not needed
        .addInterceptor(loggingInterceptor)
        .build()
}

/* Provide Retrofit */
fun provideRetrofit(factory: Gson, client: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(factory))
        .client(client)
        .build()
}