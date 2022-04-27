package ru.features.auth.domain

import ru.mvi.domain.model.AccessTokenInfo

interface AuthRepository {
    suspend fun authorize(code: String): Result<AccessTokenInfo>
}

