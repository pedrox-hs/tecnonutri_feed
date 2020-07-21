package com.pedrenrique.tecnonutri.domain.repositories

import com.pedrenrique.tecnonutri.domain.FeedItem
import com.pedrenrique.tecnonutri.domain.Profile

interface ProfileRepository {

    fun get(userId: Int, callback: ProfileCallback)

    fun loadMorePosts(
        userId: Int,
        page: Int,
        timestamp: Int,
        callback: ProfileCallback
    )

    interface ProfileCallback {
        fun onSuccess(
            profile: Profile,
            feedItems: List<FeedItem>,
            page: Int,
            timestamp: Int
        )

        fun onError(t: Throwable)
    }
}