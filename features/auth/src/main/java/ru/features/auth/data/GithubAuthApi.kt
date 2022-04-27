package ru.features.auth.data

import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.POST

data class AuthBody(
    @SerializedName("client_id")
    val clientId: String,
    @SerializedName("client_secret")
    val clientSecret: String,
    @SerializedName("code")
    val code: String
)

interface GithubAuthApi {
    @POST("/login/oauth/access_token")
    suspend fun authorize(@Body body: AuthBody): String
}
