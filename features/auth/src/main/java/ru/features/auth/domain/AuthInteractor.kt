package ru.features.auth.domain

import ru.mvi.domain.AccountManager
import ru.mvi.domain.repository.AccessTokenRepository
import javax.inject.Inject

sealed class AuthResult {
    object Success : AuthResult()
    class Failure(val error: Throwable) : AuthResult()
}

interface AuthInteractor {
    suspend fun authorize(code: String) : AuthResult
}

class AuthInteractorImpl @Inject constructor(
    private val authRepository: AuthRepository,
    private val accountManager: AccountManager,
    private val accessTokenRepository: AccessTokenRepository
) : AuthInteractor {
    override suspend fun authorize(code: String) : AuthResult {
        val result = authRepository.authorize(code)

        if (result.isFailure) {
            return AuthResult.Failure(result.exceptionOrNull()!!)
        }

        accessTokenRepository.token = result.getOrNull()
        accountManager.refresh()

        return AuthResult.Success
    }
}
