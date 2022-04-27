package ru.mvi.core.navigation

import java.util.concurrent.ConcurrentLinkedQueue
import javax.inject.Inject

private enum class CommandType {
    NAVIGATE, BACK, CLEAR;
}

private class ScreenCommand(
    val commandType: CommandType,
    val data: Any? = null
)

interface Router : Navigator {
    fun setNavigator(navigator: Navigator)
    fun removeNavigator()
}

class RouterImpl @Inject constructor() : Router {
    private var navigator: Navigator? = null
    private val commandsCache = ConcurrentLinkedQueue<ScreenCommand>()

    override fun setNavigator(navigator: Navigator) {
        this.navigator = navigator
        runUndeliveredCommands()
    }

    override fun removeNavigator() {
        this.navigator = null
    }

    override fun navigateTo(screen: Screen) {
        if (navigator == null) {
            commandsCache.add(ScreenCommand(CommandType.NAVIGATE, screen))
        } else {
            navigator?.navigateTo(screen)
        }
    }

    override fun back() {
        if (navigator == null) {
            commandsCache.add(ScreenCommand(CommandType.BACK))
        } else {
            navigator?.back()
        }
    }

    override fun clear() {
        if (navigator == null) {
            commandsCache.add(ScreenCommand(CommandType.CLEAR))
        } else {
            navigator?.clear()
        }
    }

    private fun runUndeliveredCommands() {
        while (commandsCache.isNotEmpty()) {
            val command = commandsCache.remove()
            when (command.commandType) {
                CommandType.NAVIGATE -> navigateTo(command.data as Screen)
                CommandType.BACK -> back()
                CommandType.CLEAR -> clear()
            }
        }
    }

    override fun getEntryCount(): Int {
        return navigator?.getEntryCount() ?: 0
    }
}
