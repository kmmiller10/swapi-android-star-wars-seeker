package me.philoproject.starwarsseeker.ui.utils

import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.philoproject.starwarsseeker.app.nonNullString

/**
 * Debounces text changes in a SearchView. Has a shorter duration for debouncing text changes to perform
 * local queries on cached models. Uses a longer duration to debounce a text change submission for the API call.
 */
class QueryChangeDebouncer(
    lifecycle: Lifecycle,
    private val onDebounceTextChange: (String?) -> Unit,
    private val onSubmit: (String?) -> Unit
) : SearchView.OnQueryTextListener {

    private val coroutineScope = lifecycle.coroutineScope
    private var queryJob: Job? = null
    private var lastSubmit: String? = null

    override fun onQueryTextSubmit(query: String?): Boolean {
        if(query != lastSubmit && query.nonNullString().isNotEmpty()) {
            // Don't submit an empty query or a query which was already submitted via a debounced text change
            onSubmit(query)
            lastSubmit = query
        }
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        // Cancel the previous job
        queryJob?.cancel()

        queryJob = coroutineScope.launch {
            // Debounce after half a second for local queries which will search realm
            delay(LOCAL_QUERY_DEBOUNCE_DURATION)
            if(newText != lastSubmit || newText.nonNullString().isEmpty()) {
                // Don't override the api search unless text was cleared
                onDebounceTextChange(newText?.trim()) // trim leading/trailing white space for local queries
            }

            if(newText.nonNullString().isEmpty()) {
                // The text was cleared but we do not submit an empty query to the api, so we should clear lastSubmit now
                lastSubmit = ""
                return@launch
            }

            // Debounce after another second (1.5s total) for performing the api call
            delay(REMOTE_QUERY_DEBOUNCE_DURATION)
            if(newText != lastSubmit && newText.nonNullString().isNotEmpty()) {
                // Don't submit the same query if the user already manually submitted it, or if the search is empty
                onSubmit(newText)
                lastSubmit = newText
            }
        }
        return false
    }

    companion object {
        // Amount of time to wait after last typed character to perform a local search
        private const val LOCAL_QUERY_DEBOUNCE_DURATION = 500L
        // Amount of time to wait after last typed character to submit the query to the API
        private const val REMOTE_QUERY_DEBOUNCE_DURATION = 1000L ///< LOCAL + REMOTE = 1.5 seconds total for remote search
    }
}