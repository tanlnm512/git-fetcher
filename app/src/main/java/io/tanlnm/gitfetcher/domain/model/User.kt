package io.tanlnm.gitfetcher.domain.model

import androidx.annotation.Keep

@Keep
data class User(
    val id: Long,
    val username: String,
    val avatar: String,
    val link: String?
)