package ru.mvi.api.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import ru.mvi.domain.repository.AccessTokenRepository
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val accessTokenRepository: AccessTokenRepository
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        accessTokenRepository.token?.let {
            requestBuilder.addHeader("Authorization", "Bearer ${it.token}")
        }

        return chain.proceed(requestBuilder.build())
    }
}
