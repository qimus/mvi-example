package ru.den.detail_info.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.mvi.domain.model.GithubRepositoryItem
import ru.mvi.domain.model.GithubSearchOwner

@Parcelize
data class GithubItem(
    val id: Long,
    val name: String,
    val fullName: String,
    val owner: Owner,
    val starsCount: Int,
    val watchersCount: Int,
    val forksCount: Int,
    val description: String,
    val topics: List<String>,
    val htmlUrl: String
) : Parcelable {
    companion object {
        fun from(item: GithubRepositoryItem): GithubItem = GithubItem(
            id = item.id,
            name = item.name,
            fullName = item.fullName,
            owner = Owner.from(item.owner),
            starsCount = item.starsCount,
            watchersCount = item.watchersCount,
            forksCount = item.forksCount,
            description = item.description,
            topics = item.topics,
            htmlUrl = item.htmlUrl
        )
    }
}

@Parcelize
data class Owner(
    val id: Long,
    val login: String,
    val avatarUrl: String,
    val type: String
) : Parcelable {
    companion object {
        fun from(item: GithubSearchOwner): Owner = Owner(
            id = item.id,
            login = item.login,
            avatarUrl = item.avatarUrl,
            type = item.type
        )
    }
}