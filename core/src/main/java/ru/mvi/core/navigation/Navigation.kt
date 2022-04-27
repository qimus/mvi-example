package ru.mvi.core.navigation

import ru.mvi.domain.model.GithubSearchItem

interface Navigation {
    fun search(): Screen
    fun detailsInfo(item: GithubSearchItem): Screen
    fun auth(): Screen
    fun profile(): Screen
}
