package ru.mvi.domain.repository

import ru.mvi.domain.model.GithubSearchResponse

private const val PER_PAGE = 50
private const val DEFAULT_PAGE = 1

interface GithubRepository {
    suspend fun search(
        query: String,
        page: Int = DEFAULT_PAGE,
        perPage: Int = PER_PAGE
    ): Result<GithubSearchResponse>
}
