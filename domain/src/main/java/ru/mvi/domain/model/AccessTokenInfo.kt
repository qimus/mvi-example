package ru.mvi.domain.model

import com.google.gson.annotations.SerializedName

data class AccessTokenInfo(
    @SerializedName("token")
    val token: String,
    @SerializedName("scope")
    val scope: List<String>,
    @SerializedName("token_type")
    val tokenType: String
)
