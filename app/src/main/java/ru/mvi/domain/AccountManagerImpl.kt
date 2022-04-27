package ru.mvi.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import ru.mvi.domain.model.User
import ru.mvi.domain.repository.AccessTokenRepository
import ru.mvi.domain.repository.CurrentUserRepository
import javax.inject.Inject

class AccountManagerImpl @Inject constructor(
    private val repo: CurrentUserRepository,
    private val accessTokenRepository: AccessTokenRepository
) : AccountManager {

    private val userFlow = MutableSharedFlow<User>(replay = 1)

    override suspend fun logout() {
        refresh()
        accessTokenRepository.token = null
        userFlow.emit(User.EMPTY)
    }

    override suspend fun getCurrentUser(): Flow<User> {
        userFlow.emit(repo.getCurrentUser())
        return userFlow.asSharedFlow()
    }

    override suspend fun refresh() {
        repo.invalidate()
        userFlow.emit(repo.getCurrentUser())
    }
}
