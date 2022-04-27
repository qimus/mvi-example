package ru.features.profile.di

import android.content.Context
import androidx.fragment.app.Fragment
import dagger.Component
import ru.features.profile.ProfileModuleDeps
import ru.features.profile.presentation.ProfileFragment
import ru.mvi.core.di.DependencyResolverHolder

@Component(
    dependencies = [ProfileModuleDeps::class]
)
interface ProfileComponent {

    fun inject(f: ProfileFragment)

    @Component.Builder
    interface Builder {
        fun deps(deps: ProfileModuleDeps): Builder
        fun build(): ProfileComponent
    }
}

private fun createInjector(context: Context): ProfileComponent {
    val resolver = (context.applicationContext as DependencyResolverHolder).getResolver()
    return DaggerProfileComponent
        .builder()
        .deps(resolver.resolve(ProfileModuleDeps::class.java) as ProfileModuleDeps)
        .build()
}

val Fragment.injector: ProfileComponent
    get() = createInjector(requireContext())