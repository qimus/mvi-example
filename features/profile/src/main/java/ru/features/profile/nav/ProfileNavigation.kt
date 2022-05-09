package ru.features.profile.nav

import android.util.Log
import ru.features.profile.presentation.repos.ReposFragment
import ru.mvi.core.navigation.Navigation
import ru.mvi.core.navigation.Router
import ru.mvi.core.navigation.Screen
import ru.mvi.domain.model.GithubRepositoryItem
import javax.inject.Inject

interface ProfileNavigation {
    fun navigateToDetailsInfo(item: GithubRepositoryItem)
    fun navigateToReposList(login: String)
}

class ProfileNavigationImpl @Inject constructor(
    private val router: Router,
    private val appNavigation: Navigation
) : ProfileNavigation {

    override fun navigateToDetailsInfo(item: GithubRepositoryItem) {
        Log.d("ProfileNavigation", "router: $router")
        router.navigateTo(appNavigation.detailsInfo(item))
    }

    override fun navigateToReposList(login: String) {
        Log.d("ProfileNavigation", "router: $router")
        router.navigateTo(createReposListScreen(login))
    }

    private fun createReposListScreen(login: String): Screen {
        return Screen("repos-list") {
            ReposFragment.newInstance(login)
        }
    }
}
