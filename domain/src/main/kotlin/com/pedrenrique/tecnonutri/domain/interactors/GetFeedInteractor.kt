package com.pedrenrique.tecnonutri.domain.interactors

import com.pedrenrique.tecnonutri.domain.FeedItem
import com.pedrenrique.tecnonutri.domain.interactors.base.Interactor

interface GetFeedInteractor : Interactor {

    interface Callback {

        fun onFeedRetrieved(
            feedItems: List<FeedItem>,
            page: Int,
            timestamp: Int
        )

        fun onFeedRetrieveFailed(error: Throwable)
    }
}