package ru.features.auth.di

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import ru.features.auth.Configuration
import ru.features.auth.data.GithubAuthApi

@Module
object NetworkModule {
    @Provides
    @AuthFeatureScope
    fun provideHttpClient(): OkHttpClient {
        val logInterceptor = HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BASIC)
        }

        return OkHttpClient.Builder().apply {
            addInterceptor(logInterceptor)
        }.build()
    }

    @Provides
    @AuthFeatureScope
    fun provideRetrofit(httpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Configuration.BASE_AUTH)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
    }

    @Provides
    @AuthFeatureScope
    fun provideApiClient(retrofit: Retrofit): GithubAuthApi = retrofit.create(GithubAuthApi::class.java)
}
