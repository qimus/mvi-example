package ru.mvi

import android.app.Application
import android.content.Context
import ru.mvi.core.di.DependencyResolver
import ru.mvi.core.di.DependencyResolverHolder
import ru.mvi.di.AppComponent
import ru.mvi.di.DaggerAppComponent

class MviApp : Application(), DependencyResolverHolder {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .context(this)
            .build()
    }

    override fun getResolver(): DependencyResolver = appComponent.dependencyResolver
}

val Context.appComponent: AppComponent
    get() = when (this) {
        is MviApp -> appComponent
        else -> this.applicationContext.appComponent
    }