package ru.mvi.domain.repository

import ru.mvi.domain.model.User

interface CurrentUserRepository {
    suspend fun getCurrentUser(): User
    suspend fun invalidate()
}
