package ru.features.auth.nav

import ru.features.auth.presentation.auth.AuthFragment
import ru.mvi.core.navigation.Screen

object Screens {
    fun navigateToAuth(): Screen = Screen("auth-feature-auth-screen") {
        AuthFragment.newInstance()
    }
}
