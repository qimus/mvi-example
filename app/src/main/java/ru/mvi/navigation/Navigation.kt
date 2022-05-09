package ru.mvi.navigation

import ru.den.detail_info.presentation.DetailInfoFragment
import ru.features.auth.presentation.start.StartFragment
import ru.features.profile.presentation.ProfileFragment
import ru.features.search.presentation.SearchFragment
import ru.mvi.core.navigation.Navigation

import ru.mvi.core.navigation.Screen
import ru.mvi.domain.model.GithubRepositoryItem
import javax.inject.Inject


class NavigationImpl @Inject constructor() : Navigation {
    override fun search() = Screen("search_screen") {
        SearchFragment.newInstance()
    }

    override fun detailsInfo(item: GithubRepositoryItem) = Screen("details-info") {
        DetailInfoFragment.newInstance(item)
    }

    override fun auth(): Screen = Screen("auth-screen") {
        StartFragment.newInstance()
    }

    override fun profile(): Screen = Screen("profile-screen") {
        ProfileFragment.newInstance()
    }
}
