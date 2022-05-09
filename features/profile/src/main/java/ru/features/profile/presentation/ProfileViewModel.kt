package ru.features.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.mvi.core.mvi.*
import ru.mvi.domain.AccountManager
import ru.mvi.domain.model.User
import javax.inject.Inject

sealed class UiAction : MviAction {
    class CurrentUser(val user: User): UiAction()
}

sealed class UiEvent : MviEvent {
    object ShowRepos : UiEvent()
}

sealed class UiEffect : MviEffect {
    class ShowRepos(val login: String) : UiEffect()
}

data class UiState(
    val user: User?,
    val isFetching: Boolean = false
) : MviState

class ProfileViewModel(
    private val accManager: AccountManager
) : MviViewModel<UiAction, UiState, UiEffect, UiEvent>() {

    init {
        viewModelScope.launch { events.collect(::handleEvent) }

        viewModelScope.launch {
            accManager.getCurrentUser().collect { user ->
                setAction(UiAction.CurrentUser(user))
            }
        }
    }

    fun logout() = viewModelScope.launch { accManager.logout() }

    private fun handleEvent(event: UiEvent) {
        when (event) {
            is UiEvent.ShowRepos -> setEffect {
                val login = state.value.user!!.login
                UiEffect.ShowRepos(login)
            }
        }
    }


    override fun reduceState(state: UiState, action: UiAction): UiState {
        return when (action) {
            is UiAction.CurrentUser -> state.copy(user = action.user)
        }
    }

    override fun createInitState(): UiState = UiState(user = null)

    class Factory @Inject constructor(
        private val accManager: AccountManager
    ): ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
                return ProfileViewModel(accManager) as T
            }

            throw IllegalArgumentException("Wrong view model class: $modelClass")
        }
    }
}
