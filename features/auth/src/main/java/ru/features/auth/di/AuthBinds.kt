package ru.features.auth.di

import dagger.Binds
import dagger.Module
import ru.features.auth.data.AuthRepositoryImpl
import ru.features.auth.domain.AuthInteractor
import ru.features.auth.domain.AuthInteractorImpl
import ru.features.auth.domain.AuthRepository

@Module
interface AuthBinds {
    @Binds
    fun bindGithubRepository(repository: AuthRepositoryImpl): AuthRepository

    @Binds
    fun bindsAuthInteractor(interactor: AuthInteractorImpl): AuthInteractor
}
