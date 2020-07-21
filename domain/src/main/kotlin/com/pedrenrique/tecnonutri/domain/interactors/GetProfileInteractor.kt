package com.pedrenrique.tecnonutri.domain.interactors

import com.pedrenrique.tecnonutri.domain.FeedItem
import com.pedrenrique.tecnonutri.domain.Profile

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