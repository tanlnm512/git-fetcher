package io.tanlnm.gitfetcher.domain.model

import androidx.annotation.Keep

@Keep
data class UserDetail(
    val id: Long,
    val username: String,
    val name: String,
    val avatar: String,
    val bio: String?,
    val link: String?,
    val blog: String?,
    val location: String?,
    val publicRepos: Int,
    val publicGists: Int,
    val followers: Int,
    val following: Int
)
