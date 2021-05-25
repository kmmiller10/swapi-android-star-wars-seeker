package me.philoproject.starwarsseeker.ui.list

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import io.realm.Realm
import me.philoproject.starwarsseeker.R
import me.philoproject.starwarsseeker.databinding.FragmentCharacterListBinding
import me.philoproject.starwarsseeker.remote.base.Status
import me.philoproject.starwarsseeker.remote.models.realm.CharacterModel
import me.philoproject.starwarsseeker.viewmodels.CharacterListViewModel
import me.philoproject.starwarsseeker.remote.models.realm.findCharactersByQuery
import me.philoproject.starwarsseeker.ui.detail.CharacterDetailFragment
import me.philoproject.starwarsseeker.ui.showUiErrorForAppException
import me.philoproject.starwarsseeker.ui.utils.OnModelClickListener
import me.philoproject.starwarsseeker.ui.utils.QueryChangeDebouncer
import me.philoproject.starwarsseeker.viewmodels.sortModels
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class CharacterListFragment : Fragment(), OnModelClickListener {
    private lateinit var binding: FragmentCharacterListBinding
    private val viewModel by sharedViewModel<CharacterListViewModel>()
    private var realm: Realm? = null
    private var characterListAdapter: CharacterListAdapter? = null
    private var persistQueries: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        realm = Realm.getDefaultInstance()
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCharacterListBinding.inflate(inflater, container, false)
        persistQueries = true
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapterAndObservers()
    }

    /**
     * Initializes the List Differ Adapter and the LiveData property observers
     */
    private fun initAdapterAndObservers() {
        // Set up the adapter
        characterListAdapter = CharacterListAdapter(this)
        with(binding.characterList) {
            adapter = characterListAdapter
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        }

        // Restore searches on rotate or on return to this frag
        val models = queryRealmSorted(viewModel.lastSearch)
        viewModel.restoreCharacters(models)
        updateEmptyView(models.isEmpty(), null)

        // Observe ViewModel LiveData properties to update UI on character/status changes
        viewModel.characters.observe(viewLifecycleOwner, { characterList ->
            characterListAdapter?.submitList(characterList)
        })

        viewModel.status.observe(viewLifecycleOwner, { status ->
            handleStatusUpdate(status)
        })
    }

    /**
     * Given a particular status, update the loading indicator, empty view message, and show an error message if necessary
     */
    private fun handleStatusUpdate(status: Status) {
        when(status) {
            Status.Success -> {
                // Successfully loaded results - hide empty view and message
                updateLoadingIndicator(isLoading = false)
                updateEmptyView(showEmpty = false, status)
            }
            Status.Empty -> {
                // Finished loading, but there are no results - hide loading indicator and inform user of empty results
                updateLoadingIndicator(isLoading = false)
                updateEmptyView(showEmpty = true, status)
            }
            Status.Running -> {
                // An API call is running - show loading indicator and text if cached results are not visible
                updateLoadingIndicator(isLoading = true)
                updateEmptyView(showEmpty = characterListAdapter?.currentList?.isEmpty() == true, status)
            }
            is Status.Error -> {
                // Hide loading indicator and show error message if cached results are not visible
                updateLoadingIndicator(isLoading = false)
                updateEmptyView(showEmpty = characterListAdapter?.currentList?.isEmpty() == true, status)

                // Parse the error msg and present in the UI as a dialog
                status.appError?.let {
                    requireContext().showUiErrorForAppException(it)
                }
            }
        }
    }

    /**
     * Shows/hides the empty view message and informs the user why the screen is empty
     *
     * @param showEmpty - Show or hide the empty view
     * @param status - The empty message type to show. If it is null, don't change the previous text
     */
    private fun updateEmptyView(showEmpty: Boolean, status: Status?) {
        if(showEmpty && status != null) {
            binding.emptyListText.text = when(status) {
                Status.Success -> ""
                Status.Empty -> getString(R.string.no_results)
                Status.Running -> getString(R.string.loading)
                is Status.Error -> getString(R.string.error_on_search)
            }
        }
        binding.emptyListText.visibility = if(showEmpty) View.VISIBLE else View.GONE
    }

    /**
     * Shows/hides the loading indicator
     */
    private fun updateLoadingIndicator(isLoading: Boolean) {
        binding.progressIndicator.visibility = if(isLoading) View.VISIBLE else View.GONE
    }

    /**
     * Inflates the search menu and sets up a query listener
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu)
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        setupSearchQueryListener(searchView)
    }

    /**
     * Sets up the search view. Restores the previous search and adds a debouncer to search query changes
     * so that we query realm after short delays, then make the web call after longer delays
     */
    private fun setupSearchQueryListener(searchView: SearchView) {
        searchView.post {
            // Restore previous search on rotate
            searchView.setQuery(viewModel.lastSearch, false)
        }

        searchView.setOnQueryTextListener(
            QueryChangeDebouncer(lifecycle,
                { debouncedQuery ->
                    debouncedQuery?.let { query ->
                        // Perform a local search in realm and update the UI
                        val models = queryRealmSorted(query)
                        characterListAdapter?.submitList(models)
                        if(persistQueries) viewModel.lastSearch = query

                        // Update empty view
                        val state = if(query.isEmpty()) null else Status.Running
                        updateEmptyView(showEmpty = models.isEmpty(), state)
                    }
                },
                { submittedQuery ->
                    // Longer debounce period or search was tapped - make API call to retrieve results
                    submittedQuery?.let { viewModel.performSearch(it) }
                }
            )
        )
    }

    /**
     * Performs a local search in realm
     *
     * @return The result of the query detached from realm and sorted by name
     */
    private fun queryRealmSorted(query: String): List<CharacterModel> {
        val rlm = realm ?: return listOf()
        val results = rlm.findCharactersByQuery(query)
        return rlm.copyFromRealm(results.sortModels())
    }

    /**
     * When a character is tapped on from the list, navigate to the character detail pane
     */
    override fun onModelItemClicked(modelName: String) {
        val args = Bundle().apply {
            putString(CharacterDetailFragment.ARG_CHARACTER_NAME, modelName)
        }

        // Find the proper nav controller whether in tablet or phone and navigate to the character frag
        val itemDetailFragmentContainer: View? = binding.root.findViewById(R.id.item_detail_nav_container)
        if(itemDetailFragmentContainer != null) {
            itemDetailFragmentContainer.findNavController().navigate(R.id.fragment_character_detail, args)
        } else {
            binding.root.findNavController().navigate(R.id.show_character_detail, args)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // If SearchView is open when this frag is destroyed, it will close the SearchView and submit an empty string
        // which will override the persisted query. Prevent this from happening by toggling this flag
        persistQueries = false
    }

    override fun onDestroy() {
        super.onDestroy()
        realm?.close()
        realm = null
    }

}