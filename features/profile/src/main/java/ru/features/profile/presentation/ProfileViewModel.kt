package ru.features.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.mvi.core.mvi.*
import ru.mvi.domain.AccountManager
import ru.mvi.domain.model.User
import javax.inject.Inject

sealed class UiAction : MviAction {
    class CurrentUser(val user: User): UiAction()
}

sealed class UiEvent : MviEvent {}

sealed class UiEffect : MviEffect {}

data class UiState(
    val user: User?
) : MviState

class ProfileViewModel(
    private val accManager: AccountManager
) : MviViewModel<UiAction, UiState, UiEffect, UiEvent>() {

    init {
        viewModelScope.launch {
            accManager.getCurrentUser().collect { user ->
                setAction(UiAction.CurrentUser(user))
            }
        }
    }

    fun logout() = viewModelScope.launch { accManager.logout() }

    override fun reduceState(state: UiState, action: UiAction): UiState {
        return when (action) {
            is UiAction.CurrentUser -> state.copy(user = action.user)
        }
    }

    override fun createInitState(): UiState = UiState(null)

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
