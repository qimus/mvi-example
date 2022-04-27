package ru.mvi.core.navigation

import androidx.fragment.app.Fragment

class Screen(
    val key: String,
    val factory: () -> Fragment,
) {
    fun createFragment(): Fragment = factory.invoke()
}
