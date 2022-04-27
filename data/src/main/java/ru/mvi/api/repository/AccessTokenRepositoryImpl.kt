package ru.mvi.api.repository

import android.content.SharedPreferences
import ru.mvi.core.utils.getModel
import ru.mvi.core.utils.saveModel
import ru.mvi.domain.model.AccessTokenInfo
import ru.mvi.domain.repository.AccessTokenRepository
import javax.inject.Inject

class AccessTokenRepositoryImpl @Inject constructor(
    private val sp: SharedPreferences
): AccessTokenRepository {
    companion object {
        private const val ACCESS_TOKEN_KEY = "ru.mvi.api.repository.AccessTokenRepositoryImpl"
    }

    override var token: AccessTokenInfo?
        get() = sp.getModel(ACCESS_TOKEN_KEY)
        set(value) { sp.saveModel(ACCESS_TOKEN_KEY, value) }
}
