package ru.mvi.di

import android.content.Context
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import ru.features.auth.AuthModuleDeps
import ru.features.profile.ProfileModuleDeps
import ru.features.search.SearchFeatureDeps
import ru.mvi.api.di.ApiModule
import ru.mvi.core.di.DependencyResolver
import ru.mvi.core.di.ModuleDeps
import ru.mvi.presentation.MainActivity
import javax.inject.Scope

@AppScope
@Component(
    modules = [
        DepsModule::class,
        AppModule::class,
        ApiModule::class
    ]
)
interface AppComponent {
    fun inject(activity: MainActivity)

    val dependencyResolver: DependencyResolver

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppComponent
    }
}

@Module
interface DepsModule {
    @Binds
    @[IntoMap ClassKey(SearchFeatureDeps::class)]
    fun searchDeps(deps: SearchModuleDepsImpl): ModuleDeps

    @Binds
    @[IntoMap ClassKey(AuthModuleDeps::class)]
    fun authDeps(deps: AuthModuleDepsImpl): ModuleDeps

    @Binds
    @[IntoMap ClassKey(ProfileModuleDeps::class)]
    fun profileDeps(deps: ProfileModuleDepsImpl): ModuleDeps
}


@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class AppScope
