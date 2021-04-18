package com.pedrenrique.tecnonutri.domain.repositories

import com.pedrenrique.tecnonutri.domain.FeedItem

interface FeedRepository {

    fun get(feedHash: String, callback: FeedItemCallback)

    fun likeItem(feedHash: String)

    fun dislikeItem(feedHash: String)

    fun firstList(callback: FeedCallback)

    fun loadMore(page: Int, timestamp: Int, callback: FeedCallback)

    interface FeedCallback {

        fun onSuccess(
            feedItems: List<FeedItem>,
            page: Int,
            timestamp: Int
        )

        fun onError(error: Throwable)
    }

    interface FeedItemCallback {

        fun onSuccess(feedItem: FeedItem)

        fun onError(error: Throwable)
    }
}