package ru.features.auth

import android.net.Uri
import androidx.core.net.toUri

object Configuration {
    const val DETAILS_LINK = "https://github.com/qimus/mvi-example"
    const val BASE_AUTH = "https://github.com/"
    const val CALLBACK_HOST = "localhost"

    const val SCOPE_EMAIL = "email"
    const val SCOPE_USER = "user"

    fun createAuthUrl(
        clientId: String,
        scopes: List<String> = listOf(SCOPE_EMAIL, SCOPE_USER)
    ): Uri {
        return BASE_AUTH.toUri()
            .buildUpon()
            .path("login/oauth/authorize")
            .appendQueryParameter("scope", scopes.joinToString(":"))
            .appendQueryParameter("client_id", clientId)
            .build()
    }
}
