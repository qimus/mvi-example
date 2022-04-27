package ru.features.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import ru.mvi.core.DispatcherProvider
import ru.mvi.core.mvi.*
import ru.mvi.domain.model.GithubSearchItem
import ru.mvi.domain.model.GithubSearchResponse
import ru.mvi.domain.repository.GithubRepository
import javax.inject.Inject

data class SearchState(
    val search: String = "",
    val items: List<GithubSearchItem> = listOf(),
    val isFetching: Boolean = false,
    val error: String? = null,
    val currentPage: Int = 1
) : MviState

sealed class UiEvent : MviEvent {
    class Search(val query: String) : UiEvent()
    class Scroll(
        val visibleItemCount: Int,
        val lastVisibleItemPosition: Int,
        val totalItemCount: Int
    ) : UiEvent() {
        val shouldFetchMore: Boolean
            get() = visibleItemCount + lastVisibleItemPosition + VISIBLE_THRESHOLD >= totalItemCount
    }
}

sealed class Action : MviAction {
    class ItemsLoaded(
        val items: List<GithubSearchItem>,
        val page: Int = 1
    ) : Action()
    class Error(val message: String) : Action()
    object Fetching : Action()
    class Search(val query: String) : Action()
}

sealed class UiEffect : MviEffect {
    class ShowToast(val message: String) : UiEffect()
}

@OptIn(FlowPreview::class)
class SearchViewModel private constructor(
    private val githubRepository: GithubRepository,
    private val dispatchers: DispatcherProvider
) : MviViewModel<Action, SearchState, UiEffect, UiEvent>() {

    companion object {
        private val DEFAULT_STATE = SearchState()
    }

    private val search = MutableSharedFlow<String>(replay = 1, extraBufferCapacity = 5)

    init {
        viewModelScope.launch {

            launch { search.onEach { setAction(Action.Search(it)) }.collect() }

            launch { events.collect(::handleEvent) }

            launch {
                search
                    .distinctUntilChanged()
                    .debounce(1000L)
                    .onEach {
                        if (it.isNotBlank()) {
                            setAction(Action.Fetching)
                        } else {
                            setAction(Action.ItemsLoaded(listOf()))
                        }
                    }
                    .filter { it.isNotBlank() }
                    .map { q -> githubRepository.search(q) }
                    .flowOn(dispatchers.default)
                    .collect { result -> handleSearchResult(result) }
            }
        }
    }

    override fun createInitState(): SearchState = DEFAULT_STATE

    override fun reduceState(state: SearchState, action: Action): SearchState = when (action) {
        is Action.ItemsLoaded -> {
            val items = if (action.page > 1) state.items + action.items else action.items
            state.copy(items = items, isFetching = false, currentPage = action.page)
        }
        is Action.Error -> state.copy(items = listOf(), error = action.message, isFetching = false)
        is Action.Fetching -> state.copy(isFetching = true, error = null)
        is Action.Search -> state.copy(search = action.query)
    }

    private fun handleSearchResult(result: Result<GithubSearchResponse>, page: Int = 1) {
        result.onSuccess { res ->
            setAction(Action.ItemsLoaded(items = res.items, page = page))
        }
        result.onFailure { t ->
            setEffect { UiEffect.ShowToast(t.localizedMessage ?: "error") }
            setAction(Action.Error(message = t.localizedMessage ?: "error"))
        }
    }

    private fun handleEvent(uiEvent: UiEvent) {
        when (uiEvent) {
            is UiEvent.Search -> search.tryEmit(uiEvent.query)
            is UiEvent.Scroll -> {
                if (uiEvent.shouldFetchMore) {
                    val state = this@SearchViewModel.state.value
                    if (state.isFetching) return
                    val page = state.currentPage + 1

                    setEffect { UiEffect.ShowToast("Start loading page: $page") }

                    viewModelScope.launch {
                        setAction(Action.Fetching)
                        val result = githubRepository.search(state.search, page)
                        handleSearchResult(result, page)
                    }
                }
            }
        }
    }

    class Factory @Inject constructor(
        private val githubRepo: GithubRepository,
        private val dispatcherProvider: DispatcherProvider
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
                return SearchViewModel(githubRepo, dispatcherProvider) as T
            }

            throw IllegalArgumentException("Unsupported viewModel class $modelClass")
        }
    }
}

private const val VISIBLE_THRESHOLD = 5