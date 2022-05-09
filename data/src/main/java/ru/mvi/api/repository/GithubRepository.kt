package ru.mvi.api.repository

import kotlinx.coroutines.withContext
import ru.mvi.api.data.GithubApi
import ru.mvi.core.DispatcherProvider
import ru.mvi.domain.model.GithubRepositoryItem
import ru.mvi.domain.model.GithubSearchResult
import ru.mvi.domain.repository.GithubRepository
import ru.mvi.domain.repository.SortDirection
import javax.inject.Inject

class GithubRepositoryImpl @Inject constructor(
    private val api: GithubApi,
    private val dispatchers: DispatcherProvider
) : GithubRepository {

    override suspend fun search(
        query: String,
        page: Int,
        perPage: Int
    ): Result<GithubSearchResult> =
        withContext(dispatchers.io) {
            try {
                val result = api.search(query, page, perPage)
                Result.success(result)
            } catch (t: Throwable) {
                Result.failure(t)
            }
        }

    override suspend fun getReposByUser(
        login: String,
        page: Int,
        perPage: Int,
        sort: String,
        direction: SortDirection
    ): Result<List<GithubRepositoryItem>> = withContext(dispatchers.io) {
        try {
            val result = api.getReposByUser(login, page, perPage, sort, direction.toString())
            Result.success(result)
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }
}
