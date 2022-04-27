package ru.features.auth.presentation.auth
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.features.auth.AuthConfig
import ru.features.auth.AuthNavigation
import ru.features.auth.Configuration
import ru.features.auth.di.AuthFeatureScope
import ru.features.auth.domain.AuthInteractor
import ru.features.auth.domain.AuthResult
import javax.inject.Inject

sealed class SideEffect {
    class ShowMessage(val message: String) : SideEffect()
}

class AuthViewModel(
    private val authConfig: AuthConfig,
    private val authInteractor: AuthInteractor,
    private val navigation: AuthNavigation
) : ViewModel() {

    private val _sideEffect = MutableSharedFlow<SideEffect>(replay = 1)
    val sideEffect = _sideEffect.asSharedFlow()

    fun authorize(code: String) = viewModelScope.launch {
        val result = authInteractor.authorize(code)
        when (result) {
            is AuthResult.Success -> {
                navigation.onLoginSuccess()
            }
            is AuthResult.Failure -> {
                _sideEffect.tryEmit(
                    SideEffect.ShowMessage(
                        result.error.localizedMessage ?: "Error"
                    )
                )
            }
        }
    }

    fun createAuthUrl(): String = Configuration.createAuthUrl(authConfig.clientId).toString()

    @AuthFeatureScope
    class Factory @Inject constructor(
        private val authConfig: AuthConfig,
        private val authInteractor: AuthInteractor,
        private val navigation: AuthNavigation
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
                return AuthViewModel(
                    authConfig,
                    authInteractor,
                    navigation
                ) as T
            }

            throw IllegalArgumentException("Wrong view model class $modelClass")
        }
    }
}
