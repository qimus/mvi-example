package ru.mvi.domain.repository

import ru.mvi.domain.model.AccessTokenInfo

interface AccessTokenRepository {
    var token: AccessTokenInfo?
}
