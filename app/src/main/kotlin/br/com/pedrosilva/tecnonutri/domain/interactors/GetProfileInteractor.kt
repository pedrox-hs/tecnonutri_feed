package br.com.pedrosilva.tecnonutri.domain.interactors

import br.com.pedrosilva.tecnonutri.domain.entities.FeedItem
import br.com.pedrosilva.tecnonutri.domain.entities.Profile

interface GetProfileInteractor {
    interface Callback {
        fun onProfileRetrieved(
            profile: Profile,
            feedItems: List<FeedItem>,
            page: Int,
            timestamp: Int
        )

        fun onProfileRetrieveFailed(error: Throwable)
    }
}