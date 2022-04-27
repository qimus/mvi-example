package ru.mvi.api.repository

import android.content.SharedPreferences
import android.util.Log
import kotlinx.coroutines.withContext
import ru.mvi.api.data.GithubApi
import ru.mvi.api.data.model.GithubUserResponse
import ru.mvi.core.DispatcherProvider
import ru.mvi.core.utils.getModel
import ru.mvi.core.utils.saveModel
import ru.mvi.domain.model.User
import ru.mvi.domain.repository.CurrentUserRepository
import javax.inject.Inject

class CurrentUserRepositoryImpl @Inject constructor(
    private val sp: SharedPreferences,
    private val api: GithubApi,
    private val dispatchers: DispatcherProvider
) : CurrentUserRepository {
    companion object {
        private const val USER_KEY = "ru.mvi.api.data.USER_KEY"
    }

    override suspend fun getCurrentUser(): User = withContext(dispatchers.io) {
        var user: User? = sp.getModel(USER_KEY)
        if (user != null) return@withContext user
        user = fetchCurrentUserFromNetwork()
        if (user.isAuthenticated) {
            sp.saveModel(USER_KEY, user)
        }
        user
    }

    override suspend fun invalidate() {
        sp.saveModel<User>(USER_KEY, null)
    }

    private suspend fun fetchCurrentUserFromNetwork(): User {
        return try {
            val apiUser = api.currentUser()
            apiUser.toUser()
        } catch (t: Throwable) {
            Log.e("GithubRepository", "error on get current user", t)
            User.EMPTY
        }
    }
}

private fun GithubUserResponse.toUser(): User = User(
    id = this.id,
    name = this.name,
    avatar = this.avatar,
    login = this.login,
    company = this.company
)
