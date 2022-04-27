package ru.mvi.api.repository

import kotlinx.coroutines.withContext
import ru.mvi.api.data.GithubApi
import ru.mvi.core.DispatcherProvider
import ru.mvi.domain.model.GithubSearchResponse
import ru.mvi.domain.repository.GithubRepository
import javax.inject.Inject

class GithubRepositoryImpl @Inject constructor(
    private val api: GithubApi,
    private val dispatchers: DispatcherProvider
) : GithubRepository {

    override suspend fun search(
        query: String,
        page: Int,
        perPage: Int
    ): Result<GithubSearchResponse> =
        withContext(dispatchers.io) {
            try {
                val result = api.search(query, page, perPage)
                Result.success(result)
            } catch (t: Throwable) {
                Result.failure(t)
            }
        }
}
