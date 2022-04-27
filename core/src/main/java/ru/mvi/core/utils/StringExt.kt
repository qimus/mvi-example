package ru.mvi.core.utils

fun makeLink(href: String, title: String? = null): String =
    "<a href=\"$href\">${title ?: href}</a>"

fun String.asHref(title: String? = null): String {
    return makeLink(this, title)
}