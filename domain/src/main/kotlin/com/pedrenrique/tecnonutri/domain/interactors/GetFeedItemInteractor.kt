package com.pedrenrique.tecnonutri.domain.interactors

import com.pedrenrique.tecnonutri.domain.FeedItem
import com.pedrenrique.tecnonutri.domain.interactors.base.Interactor

interface GetFeedItemInteractor :
    Interactor<GetFeedItemInteractor.Params, GetFeedItemInteractor.Callback> {

    class Params(val feedHash: String)

    interface Callback {
        fun onFeedItemRetrieved(feedItem: FeedItem)
        fun onFeedItemRetrieveFailed(error: Throwable)
    }
}