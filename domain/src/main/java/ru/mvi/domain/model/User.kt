package ru.mvi.domain.model

data class User(
    val id: Int,
    val login: String,
    val name: String,
    val avatar: String,
    val company: String
) {
    companion object {
        val EMPTY = User(
            id = -1,
            login = "non_auth",
            name = "non_auth",
            avatar = "",
            company = ""
        )
    }

    val isAuthenticated: Boolean
        get() = id > 0
}
