package ru.mvi.core.navigation

interface Navigator {
    fun navigateTo(screen: Screen)
    fun back()
    fun getEntryCount(): Int
    fun clear()
}
