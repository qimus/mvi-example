package ru.mvi.domain.repository

enum class SortDirection(val value: String) {
    ASC("asc"), DESC("desc");

    override fun toString(): String = value
}
