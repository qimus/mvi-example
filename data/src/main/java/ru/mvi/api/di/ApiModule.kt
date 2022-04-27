package ru.mvi.api.di

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.mvi.api.data.GithubApi
import ru.mvi.api.interceptor.AuthInterceptor
import ru.mvi.domain.repository.AccessTokenRepository
import javax.inject.Scope

private const val GITHUB_BASE_URL = "https://api.github.com"

@Module
object ApiModule {

    @Provides
    fun provideHttpClient(interceptors: @JvmSuppressWildcards Set<Interceptor>): OkHttpClient {
        return OkHttpClient.Builder().apply {
            interceptors.forEach { interceptor ->
                addInterceptor(interceptor)
            }
        }.build()
    }

    @Provides
    @IntoSet
    fun provideLogInterceptor(): Interceptor {
        return HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BASIC)
        }
    }

    @Provides
    @IntoSet
    fun provideAuthInterceptor(tokenRepository: AccessTokenRepository): Interceptor {
        return AuthInterceptor(tokenRepository)
    }

    @Provides
    fun provideRetrofit(httpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(GITHUB_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
    }

    @Provides
    fun provideGitHubApi(retrofit: Retrofit): GithubApi {
        return retrofit.create(GithubApi::class.java)
    }
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ApiScope