package ru.features.auth.di

import android.content.Context
import androidx.fragment.app.Fragment
import dagger.Component
import ru.features.auth.AuthModuleDeps
import ru.features.auth.presentation.auth.AuthFragment
import ru.features.auth.presentation.start.StartFragment
import ru.mvi.core.di.DependencyResolverHolder
import javax.inject.Scope

@Component(
    modules = [NetworkModule::class, AuthBinds::class],
    dependencies = [AuthModuleDeps::class]
)
@AuthFeatureScope
interface AuthComponent {
    fun inject(f: AuthFragment)
    fun inject(f: StartFragment)

    @Component.Builder
    interface Builder {
        fun deps(deps: AuthModuleDeps): Builder
        fun build(): AuthComponent
    }
}

@Scope
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class AuthFeatureScope

private fun createInjector(context: Context): AuthComponent {
    val resolver = (context.applicationContext as DependencyResolverHolder).getResolver()
    return DaggerAuthComponent
        .builder()
        .deps(resolver.resolve(AuthModuleDeps::class.java) as AuthModuleDeps)
        .build()
}

val Fragment.injector: AuthComponent
    get() = createInjector(requireContext())