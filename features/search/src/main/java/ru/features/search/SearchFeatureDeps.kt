package ru.features.search

import ru.mvi.core.DispatcherProvider
import ru.mvi.core.di.ModuleDeps
import ru.mvi.core.navigation.Navigation
import ru.mvi.core.navigation.Router
import ru.mvi.domain.repository.GithubRepository

interface SearchFeatureDeps : ModuleDeps {
    val githubRepo: GithubRepository
    val dispatcherProvider: DispatcherProvider
    val navigation: Navigation
    val router: Router
}
