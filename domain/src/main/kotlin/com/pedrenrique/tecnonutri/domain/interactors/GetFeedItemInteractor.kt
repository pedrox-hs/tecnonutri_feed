package com.pedrenrique.tecnonutri.domain.interactors

import com.pedrenrique.tecnonutri.domain.FeedItem

interface GetFeedItemInteractor {
    interface Callback {
        fun onFeedItemRetrieved(feedItem: FeedItem)
        fun onFeedItemRetrieveFailed(error: Throwable)
    }
}