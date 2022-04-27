package ru.mvi.di

import dagger.Binds
import dagger.Module
import ru.mvi.DispatcherProviderImpl
import ru.mvi.api.repository.AccessTokenRepositoryImpl
import ru.mvi.api.repository.CurrentUserRepositoryImpl
import ru.mvi.api.repository.GithubRepositoryImpl
import ru.mvi.core.DispatcherProvider
import ru.mvi.core.di.DependencyResolver
import ru.mvi.core.navigation.Navigation
import ru.mvi.core.navigation.Router
import ru.mvi.core.navigation.RouterImpl
import ru.mvi.di.resolver.DependencyResolverImpl
import ru.mvi.domain.AccountManager
import ru.mvi.domain.AccountManagerImpl
import ru.mvi.domain.repository.AccessTokenRepository
import ru.mvi.domain.repository.CurrentUserRepository
import ru.mvi.domain.repository.GithubRepository
import ru.mvi.navigation.NavigationImpl

@Module
interface AppBinds {
    @Binds
    @AppScope
    fun bindsNavigation(nav: NavigationImpl): Navigation

    @Binds
    @AppScope
    fun bindsDependencyResolver(resolver: DependencyResolverImpl): DependencyResolver

    @Binds
    @AppScope
    fun bindsRouter(router: RouterImpl): Router

    @Binds
    @AppScope
    fun bindsCurrentUserRepository(repo: CurrentUserRepositoryImpl): CurrentUserRepository

    @Binds
    @AppScope
    fun bindsAccessTokenRepository(repo: AccessTokenRepositoryImpl): AccessTokenRepository

    @Binds
    @AppScope
    fun bindGithubRepository(githubRepositoryImpl: GithubRepositoryImpl): GithubRepository

    @Binds
    @AppScope
    fun bindsDispatcherProvider(provider: DispatcherProviderImpl): DispatcherProvider

    @Binds
    @AppScope
    fun bindsAccountManager(accManager: AccountManagerImpl): AccountManager
}
