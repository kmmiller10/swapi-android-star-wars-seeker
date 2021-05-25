package me.philoproject.starwarsseeker.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import io.realm.Realm
import me.philoproject.starwarsseeker.R
import me.philoproject.starwarsseeker.app.nonNullString
import me.philoproject.starwarsseeker.databinding.FragmentCharacterDetailBinding
import me.philoproject.starwarsseeker.remote.models.realm.findAllCharacters
import me.philoproject.starwarsseeker.remote.models.realm.findCharacterByName
import me.philoproject.starwarsseeker.ui.detail.PlanetDetailFragment.Companion.ARG_PLANET_NAME
import me.philoproject.starwarsseeker.ui.formatString
import me.philoproject.starwarsseeker.viewmodels.CharacterViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CharacterDetailFragment : Fragment() {
    private lateinit var binding: FragmentCharacterDetailBinding
    private val viewModel by viewModel<CharacterViewModel>()
    private var realm: Realm? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        realm = Realm.getDefaultInstance()

        val characterName = arguments?.get(ARG_CHARACTER_NAME).nonNullString().trim()
        if(characterName.isNotEmpty()) {
            // Retrieve the character from realm and map into this fragment's view model
            realm?.findCharacterByName(characterName)?.let {
                viewModel.fromRealmModel(it)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCharacterDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeUi(hasContent = viewModel.name.isNotEmpty())
        fetchHomePlanetAndObserve()
    }

    /**
     * Initializes the UI state, either showing an empty view or loading the details of this Character
     */
    private fun initializeUi(hasContent: Boolean) {
        if(hasContent) {
            updateEmptyViewVisibility(showEmpty = false)
            updateDetails()
        } else {
            // No model was retrieved - this can occur in SplitView before a search result is selected
            updateEmptyViewVisibility(showEmpty = true)

            // Update the empty text based on whether characters exist in realm or not
            val hasCachedResults = realm?.findAllCharacters()?.isNotEmpty() ?: false
            val emptyText = if(hasCachedResults) {
                getString(R.string.initial_empty_detail_msg)
            } else {
                getString(R.string.initial_empty_detail_search_first_msg)
            }
            binding.emptyDetail?.text = emptyText
        }
    }

    /**
     * Fetches the home planet data for this character
     */
    private fun fetchHomePlanetAndObserve() {
        viewModel.planetName.observe(viewLifecycleOwner, { planetName ->
            updateHomeWorld(planetName)
        })
        viewModel.fetchPlanet()
    }

    /**
     * Updates the UI views with the character's details
     */
    private fun updateDetails() {
        with(binding) {
            // The character name will either go in the toolbar for phone screens, or into the layout for splitview
            toolbarLayout?.title = viewModel.name
            name?.text = viewModel.name

            // The rest of the properties are shared between the 2 layouts
            gender.text = formatString(R.string.gender_s, viewModel.gender)
            birthYear.text = formatString(R.string.born_s, viewModel.birthYear)
            mass.text = formatString(R.string.mass_s, viewModel.mass)
            height.text = formatString(R.string.height_s, viewModel.height)
            hairColor.text = formatString(R.string.hair_color_s, viewModel.hairColor)
            skinColor.text = formatString(R.string.skin_color_s, viewModel.skinColor)
            eyeColor.text = formatString(R.string.eye_color_s, viewModel.eyeColor)
        }
    }

    /**
     * When a planet is found, updates the UI with the planet name and adds a click listener.
     * If there is no planet data, or the planet is "Unknown", then don't show the planet details in the UI
     */
    private fun updateHomeWorld(planetName: String) {
        if(planetName.isNotEmpty() && !planetName.equals(UNKNOWN_PLANET, ignoreCase = true)) {
            binding.homePlanet.text = formatString(R.string.home_world_s, planetName)
            binding.homePlanet.visibility = View.VISIBLE
            binding.homePlanet.setOnClickListener {
                viewHomePlanet(planetName)
            }
        }
    }

    /**
     * Shows/hides the empty view
     */
    private fun updateEmptyViewVisibility(showEmpty: Boolean) {
        with(binding) {
            content.visibility = if(showEmpty) View.GONE else View.VISIBLE
            emptyDetail?.visibility = if(showEmpty) View.VISIBLE else View.GONE
        }
    }

    /**
     * Navigate to the home planet details page
     */
    private fun viewHomePlanet(planetName: String) {
        val args = Bundle().apply {
            putString(ARG_PLANET_NAME, planetName)
        }

        // Find the proper nav controller whether in tablet or phone and navigate to the planet frag
        val itemDetailFragmentContainer: View? = binding.root.findViewById(R.id.item_detail_nav_container)
        if(itemDetailFragmentContainer != null) {
            itemDetailFragmentContainer.findNavController().navigate(R.id.fragment_planet_detail, args)
        } else {
            binding.root.findNavController().navigate(R.id.show_planet_detail, args)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm?.close()
        realm = null
    }

    companion object {
        const val ARG_CHARACTER_NAME = "character_name"
        const val UNKNOWN_PLANET = "unknown"
    }
}