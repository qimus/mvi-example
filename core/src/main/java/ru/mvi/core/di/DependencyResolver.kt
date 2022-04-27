package ru.mvi.core.di

interface ModuleDeps

interface DependencyResolverHolder {
    fun getResolver(): DependencyResolver
}

interface DependencyResolver {
    fun resolve(clazz: Class<out ModuleDeps>): ModuleDeps
}
