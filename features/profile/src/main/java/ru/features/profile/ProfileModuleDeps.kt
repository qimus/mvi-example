package ru.features.profile

import ru.mvi.core.di.ModuleDeps
import ru.mvi.domain.AccountManager

interface ProfileModuleDeps : ModuleDeps {
    val accountManager: AccountManager
}
