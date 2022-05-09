package ru.features.profile.di

import dagger.Binds
import dagger.Module
import ru.features.profile.nav.ProfileNavigation
import ru.features.profile.nav.ProfileNavigationImpl
import ru.mvi.core.navigation.Router
import ru.mvi.core.navigation.RouterImpl
import javax.inject.Singleton

@Module
interface ProfileBinds {
    @Binds
    @Singleton
    fun bindRouter(router: RouterImpl) : Router

    @Binds
    @Singleton
    fun bindNavigation(nav: ProfileNavigationImpl): ProfileNavigation
}
