package ru.mvi.core.navigation

import ru.mvi.domain.model.GithubRepositoryItem

interface Navigation {
    fun search(): Screen
    fun detailsInfo(item: GithubRepositoryItem): Screen
    fun auth(): Screen
    fun profile(): Screen
}
