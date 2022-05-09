package ru.mvi.di

import ru.features.auth.AuthConfig
import ru.features.auth.AuthModuleDeps
import ru.features.auth.AuthNavigation
import ru.features.profile.ProfileModuleDeps
import ru.features.search.SearchFeatureDeps
import ru.mvi.Configuration
import ru.mvi.core.DispatcherProvider
import ru.mvi.core.navigation.Navigation
import ru.mvi.core.navigation.Router
import ru.mvi.domain.AccountManager
import ru.mvi.domain.repository.AccessTokenRepository
import ru.mvi.domain.repository.GithubRepository
import javax.inject.Inject

@AppScope
class SearchModuleDepsImpl @Inject constructor(
    override val githubRepo: GithubRepository,
    override val dispatcherProvider: DispatcherProvider,
    override val router: Router,
    override val navigation: Navigation
) : SearchFeatureDeps

@AppScope
class AuthModuleDepsImpl @Inject constructor(
    override val accountManager: AccountManager,
    override val accessTokenRepository: AccessTokenRepository,
    private val nav: Navigation,
    override val router: Router,
    override val dispatchers: DispatcherProvider
) : AuthModuleDeps {
    override val config: AuthConfig = object : AuthConfig {
        override val clientId: String = Configuration.Auth.AUTH_CLIENT_ID
        override val secret: String = Configuration.Auth.AUTH_SECRET
    }

    override val navigation: AuthNavigation = object : AuthNavigation {
        override fun onLoginSuccess() {
            router.navigateTo(nav.search())
        }
    }
}

class ProfileModuleDepsImpl @Inject constructor(
    override val accountManager: AccountManager,
    override val githubRepository: GithubRepository,
    override val navigation: Navigation
) : ProfileModuleDeps
