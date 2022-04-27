package ru.mvi.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import ru.mvi.core.navigation.Navigation
import ru.mvi.core.navigation.Router
import ru.mvi.domain.AccountManager
import javax.inject.Inject

class MainViewModel(
    private val router: Router,
    private val accountManager: AccountManager,
    private val navigation: Navigation
) : ViewModel() {

    fun bootstrap() {
        viewModelScope.launch {
            accountManager.getCurrentUser()
                .distinctUntilChanged()
                .collect { user ->
                    router.clear()
                    Log.d("DAGGER_LOG", "user changed: $user")
                    if (user.isAuthenticated) {
                        router.navigateTo(navigation.search())
                    } else {
                        router.navigateTo(navigation.auth())
                    }
                }
        }
    }

    class Factory @Inject constructor(
        private val router: Router,
        private val accountManager: AccountManager,
        private val navigation: Navigation
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                return MainViewModel(router, accountManager, navigation) as T
            }

            throw IllegalArgumentException("Wrong view model class $modelClass")
        }
    }
}
