package br.com.pedrosilva.tecnonutri.domain.repositories

import br.com.pedrosilva.tecnonutri.domain.entities.FeedItem
import br.com.pedrosilva.tecnonutri.domain.entities.Profile

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