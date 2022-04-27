package ru.features.auth.data

import kotlinx.coroutines.withContext
import ru.features.auth.AuthConfig
import ru.features.auth.di.AuthFeatureScope
import ru.features.auth.domain.AuthRepository
import ru.mvi.core.DispatcherProvider
import ru.mvi.domain.model.AccessTokenInfo
import javax.inject.Inject

@AuthFeatureScope
class AuthRepositoryImpl @Inject constructor(
    private val authConfig: AuthConfig,
    private val githubApi: GithubAuthApi,
    private val dispatcherProvider: DispatcherProvider
) : AuthRepository {
    override suspend fun authorize(code: String): Result<AccessTokenInfo> =
        withContext(dispatcherProvider.io) {
            return@withContext try {
                val requestBody = AuthBody(
                    clientId = authConfig.clientId,
                    clientSecret = authConfig.secret,
                    code = code
                )
                val response = githubApi.authorize(requestBody)

                val params = response.split("&").associate {
                    val (key, value) = it.split("=")
                    key to value
                }

                Result.success(
                    AccessTokenInfo(
                        token = params["access_token"] ?: "",
                        scope = params["scope"]?.split(":") ?: listOf(),
                        tokenType = params["token_type"] ?: ""
                    )
                )
            } catch (e: Throwable) {
                Result.failure(e)
            }
        }
}
