package ru.features.auth

import ru.mvi.core.DispatcherProvider
import ru.mvi.core.di.ModuleDeps
import ru.mvi.core.navigation.Router
import ru.mvi.domain.AccountManager
import ru.mvi.domain.repository.AccessTokenRepository

interface AuthConfig {
    val clientId: String
    val secret: String
}

interface AuthNavigation {
    fun onLoginSuccess()
}

interface AuthModuleDeps : ModuleDeps {
    val config: AuthConfig
    val accountManager: AccountManager
    val accessTokenRepository: AccessTokenRepository
    val navigation: AuthNavigation
    val router: Router
    val dispatchers: DispatcherProvider
}
