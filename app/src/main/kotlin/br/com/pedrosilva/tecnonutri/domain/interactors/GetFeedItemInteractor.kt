package br.com.pedrosilva.tecnonutri.domain.interactors

import br.com.pedrosilva.tecnonutri.domain.entities.FeedItem

interface GetFeedItemInteractor {
    interface Callback {
        fun onFeedItemRetrieved(feedItem: FeedItem)
        fun onFeedItemRetrieveFailed(error: Throwable)
    }
}