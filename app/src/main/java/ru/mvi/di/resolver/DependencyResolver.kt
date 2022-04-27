package ru.mvi.di.resolver

import ru.mvi.core.di.DependencyResolver
import ru.mvi.core.di.ModuleDeps
import javax.inject.Inject

class DependencyResolverImpl @Inject constructor(
    private val modules: Map<Class<*>, @JvmSuppressWildcards ModuleDeps>
) : DependencyResolver {

    override fun resolve(clazz: Class<out ModuleDeps>): ModuleDeps {
        if (!modules.containsKey(clazz)) {
            throw IllegalArgumentException("No module for $clazz found")
        }

        return modules[clazz] as ModuleDeps
    }
}
