package ru.mvi.domain.model

import com.google.gson.annotations.SerializedName

data class GithubSearchResponse(
    @SerializedName("total_count")
    val totalCount: Int,
    @SerializedName("items")
    val items: List<GithubSearchItem>
)

data class GithubSearchItem(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("owner")
    val owner: GithubSearchOwner,
    @SerializedName("stargazers_count")
    val starsCount: Int,
    @SerializedName("watchers_count")
    val watchersCount: Int,
    @SerializedName("forks_count")
    val forksCount: Int,
    @SerializedName("description")
    val description: String,
    @SerializedName("topics")
    val topics: List<String>,
    @SerializedName("html_url")
    val htmlUrl: String
)

data class GithubSearchOwner(
    @SerializedName("id")
    val id: Long,
    @SerializedName("login")
    val login: String,
    @SerializedName("avatar_url")
    val avatarUrl: String,
    @SerializedName("type")
    val type: String
)
