package ru.mvi.domain

import kotlinx.coroutines.flow.Flow
import ru.mvi.domain.model.User

interface AccountManager {
    suspend fun logout()
    suspend fun getCurrentUser(): Flow<User>
    suspend fun refresh()
}
