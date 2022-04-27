package ru.features.search.di

import dagger.Binds
import dagger.Module
import ru.features.search.nav.SearchNavigation
import ru.features.search.nav.SearchNavigationImpl

@Module
interface SearchBinds {
    @Binds
    fun bindsNavigation(nav: SearchNavigationImpl) : SearchNavigation
}
