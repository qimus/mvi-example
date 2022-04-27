package ru.mvi.api.data.model

import com.google.gson.annotations.SerializedName

data class GithubUserResponse(
    @SerializedName("login")
    val login: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("avatar_url")
    val avatar: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("company")
    val company: String
)