package ru.features.search.di

import androidx.fragment.app.Fragment
import dagger.Component
import ru.features.search.SearchFeatureDeps
import ru.features.search.presentation.SearchFragment
import ru.mvi.core.di.DependencyResolverHolder
import javax.inject.Scope

@Component(
    modules = [SearchBinds::class],
    dependencies = [SearchFeatureDeps::class]
)
@FeatureSearchScope
interface FeatureSearchComponent {
    fun inject(fragment: SearchFragment)

    @Component.Builder
    interface Builder {
        fun deps(deps: SearchFeatureDeps): Builder
        fun build(): FeatureSearchComponent
    }
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class FeatureSearchScope

val Fragment.injector: FeatureSearchComponent
    get() {
        val resolver = (requireContext().applicationContext as DependencyResolverHolder)
            .getResolver()
        return DaggerFeatureSearchComponent.builder()
            .deps(resolver.resolve(SearchFeatureDeps::class.java) as SearchFeatureDeps)
            .build()
    }