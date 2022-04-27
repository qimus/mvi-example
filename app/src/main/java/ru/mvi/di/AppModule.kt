package ru.mvi.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides

@Module(includes = [AppBinds::class])
object AppModule {
    private const val DEFAULT_SHARED_PREF_NAME = "default"

    @Provides
    @AppScope
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(DEFAULT_SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }
}
