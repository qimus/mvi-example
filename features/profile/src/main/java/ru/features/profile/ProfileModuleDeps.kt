package ru.features.profile

import ru.mvi.core.di.ModuleDeps
import ru.mvi.core.navigation.Navigation
import ru.mvi.core.navigation.Router
import ru.mvi.domain.AccountManager
import ru.mvi.domain.repository.GithubRepository

interface ProfileModuleDeps : ModuleDeps {
    val accountManager: AccountManager
    val githubRepository: GithubRepository
    val navigation: Navigation
}
