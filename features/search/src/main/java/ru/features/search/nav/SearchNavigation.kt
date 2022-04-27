package ru.features.search.nav

import ru.mvi.core.navigation.Navigation
import ru.mvi.core.navigation.Router
import ru.mvi.domain.model.GithubSearchItem
import javax.inject.Inject

interface SearchNavigation {
    fun navigateToDetails(item: GithubSearchItem)
    fun navigateToProfile()
}

class SearchNavigationImpl @Inject constructor(
    private val router: Router,
    private val navigation: Navigation
) : SearchNavigation {
    override fun navigateToDetails(item: GithubSearchItem) {
        router.navigateTo(navigation.detailsInfo(item))
    }

    override fun navigateToProfile() {
        router.navigateTo(navigation.profile())
    }
}
