package ru.mvi.core.navigation

import androidx.fragment.app.FragmentManager

class AppNavigator(
    var fragmentManager: FragmentManager,
    var containerId: Int = 0
) : Navigator {
    override fun navigateTo(screen: Screen) {
        val fragment = screen.createFragment()
        fragmentManager.beginTransaction()
            .replace(containerId, screen.createFragment(), fragment.tag)
            .addToBackStack(screen.key)
            .setReorderingAllowed(true)
            .commit()
    }

    override fun back() {
        fragmentManager.popBackStack()
    }

    override fun getEntryCount(): Int {
        return fragmentManager.backStackEntryCount
    }

    override fun clear() {
        if (getEntryCount() > 0) {
            val entry = fragmentManager.getBackStackEntryAt(0)
            fragmentManager.popBackStack(entry.id, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }
}
