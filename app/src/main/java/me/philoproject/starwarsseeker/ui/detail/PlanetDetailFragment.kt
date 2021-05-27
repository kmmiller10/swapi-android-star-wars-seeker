package me.philoproject.starwarsseeker.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import me.philoproject.starwarsseeker.R
import me.philoproject.starwarsseeker.app.nonNullString
import me.philoproject.starwarsseeker.databinding.FragmentPlanetDetailBinding
import me.philoproject.starwarsseeker.ui.formatString
import me.philoproject.starwarsseeker.viewmodels.PlanetViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Fragment that displays Planet info in the detail frag component
 */
class PlanetDetailFragment : Fragment() {
    private lateinit var binding: FragmentPlanetDetailBinding
    private val viewModel by viewModel<PlanetViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Retrieve the Planet name, then provide to the view model to load data from the DTO
        val planetName = arguments?.get(ARG_PLANET_NAME).nonNullString().trim()
        if(planetName.isNotEmpty()) {
            viewModel.loadPlanetFromRealm(planetName)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPlanetDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateDetails()
    }

    /**
     * Updates the UI with the planet's details
     */
    private fun updateDetails() {
        with(binding) {
            // The planet name will either go in the toolbar for phone screens, or into the layout for splitview
            toolbarLayout?.title = viewModel.name
            name?.text = viewModel.name

            // The rest of the properties are shared between the 2 layouts
            climate.text = formatString(R.string.climate_s, viewModel.climate)
            terrain.text = formatString(R.string.terrain_s, viewModel.terrain)
            diameter.text = formatString(R.string.diameter_s, viewModel.diameter, R.string.diameter_unknown)
            population.text = formatString(R.string.population_s, viewModel.population, R.string.population_unknown)
        }
    }

    companion object {
        const val ARG_PLANET_NAME = "planet_name"
    }
}