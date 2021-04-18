package com.pedrenrique.tecnonutri.domain.interactors

import com.pedrenrique.tecnonutri.domain.FeedItem
import com.pedrenrique.tecnonutri.domain.interactors.base.Interactor

interface GetFeedInteractor : Interactor<GetFeedInteractor.Params, GetFeedInteractor.Callback> {

    class Params(
        val page: Int = 0,
        val timestamp: Int = 0
    )

    interface Callback {

        fun onFeedRetrieved(
            feedItems: List<FeedItem>,
            page: Int,
            timestamp: Int
        )

        fun onFeedRetrieveFailed(error: Throwable)
    }
}