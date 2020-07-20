package br.com.pedrosilva.tecnonutri.domain.interactors

import br.com.pedrosilva.tecnonutri.domain.entities.FeedItem
import br.com.pedrosilva.tecnonutri.domain.interactors.base.Interactor

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