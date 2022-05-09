package ru.mvi.api.data

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.mvi.api.data.model.GithubUserResponse
import ru.mvi.domain.model.GithubRepositoryItem
import ru.mvi.domain.model.GithubSearchResult

interface GithubApi {
    @GET("search/repositories?sort=stars")
    suspend fun search(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") itemsPerPage: Int
    ): GithubSearchResult

    @GET("/user")
    suspend fun currentUser(): GithubUserResponse

    @GET("/users/{user}/repos")
    suspend fun getReposByUser(
        @Path("user") login: String,
        @Query("page") page: Int,
        @Query("per_page") itemsPerPage: Int,
        @Query("sort") sort: String,
        @Query("direction") sortDirection: String
    ): List<GithubRepositoryItem>
}
