package me.philoproject.starwarsseeker.app.modules

import me.philoproject.starwarsseeker.viewmodels.CharacterListViewModel
import me.philoproject.starwarsseeker.viewmodels.CharacterViewModel
import me.philoproject.starwarsseeker.viewmodels.PlanetViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    // Character
    viewModel { CharacterListViewModel(get()) }
    viewModel { CharacterViewModel(get()) }

    // Planet
    viewModel { PlanetViewModel() }
}