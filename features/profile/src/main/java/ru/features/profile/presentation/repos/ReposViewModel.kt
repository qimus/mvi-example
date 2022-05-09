package ru.features.profile.presentation.repos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.mvi.core.mvi.*
import ru.mvi.domain.model.GithubRepositoryItem
import ru.mvi.domain.repository.GithubRepository
import ru.mvi.domain.repository.SortDirection
import java.util.concurrent.CancellationException
import javax.inject.Inject

data class ReposUiState(
    val login: String,
    val repos: List<GithubRepositoryItem>,
    val isFetching: Boolean = false,
    val error: String? = null,
    val sort: SortOptions
) : MviState

sealed class ReposUiEvent : MviEvent {
    class FetchRepos(val login: String) : ReposUiEvent()
    class UpdateSort(
        val field: String? = null,
        val direction: SortDirection? = null
    ) : ReposUiEvent()
}

sealed class ReposUiEffect : MviEffect {
    class ShowError(val message: String) : ReposUiEffect()
    object ScrollToTop : ReposUiEffect()
}

sealed class ReposUiAction : MviAction {
    class Loading(val login: String) : ReposUiAction()
    class ReposLoaded(val items: List<GithubRepositoryItem>) : ReposUiAction()
    class Error(val message: String) : ReposUiAction()
    class UpdateSort(val sort: SortOptions) : ReposUiAction()
}

class ReposViewModel(
    private val repo: GithubRepository,
) : MviViewModel<ReposUiAction, ReposUiState, ReposUiEffect, ReposUiEvent>() {

    private var fetchJob: Job? = null

    init {
        viewModelScope.launch { events.distinctUntilChanged().collect(::handleEvent) }
    }

    private fun fetchRepos(login: String, sort: SortOptions) {
        fetchJob?.cancel(CancellationException("Filter was changed"))

        fetchJob = viewModelScope.launch {
            setAction(ReposUiAction.Loading(login))

            val result = repo.getReposByUser(
                login = login,
                sort = sort.field,
                direction = sort.direction
            )

            result.onSuccess {
                setAction(ReposUiAction.ReposLoaded(items = it))
                setEffect { ReposUiEffect.ScrollToTop }
            }

            result.onFailure {
                setAction(ReposUiAction.Error(message = it.localizedMessage ?: "Error on fetch repos"))
            }
        }
    }

    override fun reduceState(state: ReposUiState, action: ReposUiAction): ReposUiState {
        return when(action) {
            is ReposUiAction.Loading -> state.copy(isFetching = true, login = action.login)
            is ReposUiAction.ReposLoaded -> state.copy(isFetching = false, repos = action.items, error = null)
            is ReposUiAction.Error -> state.copy(isFetching = false, error = action.message)
            is ReposUiAction.UpdateSort -> state.copy(sort = action.sort)
        }
    }

    override fun createInitState(): ReposUiState = ReposUiState(
        login = "",
        repos = listOf(),
        isFetching = false,
        sort = SortOptions(
            field = SortOptions.FIELD_FULL_NAME,
            direction = SortDirection.ASC
        )
    )

    private fun handleEvent(event: ReposUiEvent) {
        when (event) {
            is ReposUiEvent.FetchRepos -> fetchRepos(event.login, state.value.sort)
            is ReposUiEvent.UpdateSort -> {
                val sort = SortOptions(
                    field = event.field ?: state.value.sort.field,
                    direction = event.direction ?: state.value.sort.direction
                )
                setAction(ReposUiAction.UpdateSort(sort))
                fetchRepos(state.value.login, sort)
            }
        }
    }

    class Factory @Inject constructor(
        private val repo: GithubRepository
    ): ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ReposViewModel::class.java)) {
                return ReposViewModel(repo) as T
            }

            throw IllegalArgumentException("Wrong view model class: ${modelClass::class.java}")
        }
    }
}
