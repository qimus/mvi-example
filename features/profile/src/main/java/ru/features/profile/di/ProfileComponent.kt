package ru.features.profile.di

import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import dagger.Component
import ru.features.profile.ProfileModuleDeps
import ru.features.profile.presentation.ProfileFragment
import ru.features.profile.presentation.ReposBottomSheetModal
import ru.features.profile.presentation.repos.ReposFragment
import ru.mvi.core.di.DependencyResolverHolder
import javax.inject.Singleton

@Component(
    modules = [ProfileBinds::class],
    dependencies = [ProfileModuleDeps::class]
)
@Singleton
interface ProfileComponent {

    fun inject(f: ProfileFragment)
    fun inject(f: ReposBottomSheetModal)
    fun inject(fragment: ReposFragment)

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
