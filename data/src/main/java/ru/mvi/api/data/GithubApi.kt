package ru.mvi.api.data

import retrofit2.http.GET
import retrofit2.http.Query
import ru.mvi.api.data.model.GithubUserResponse
import ru.mvi.domain.model.GithubSearchResponse

interface GithubApi {
    @GET("search/repositories?sort=stars")
    suspend fun search(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") itemsPerPage: Int
    ): GithubSearchResponse

    @GET("/user")
    suspend fun currentUser(): GithubUserResponse
}
