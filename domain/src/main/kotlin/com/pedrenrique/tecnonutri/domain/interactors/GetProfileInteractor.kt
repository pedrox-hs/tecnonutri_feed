package com.pedrenrique.tecnonutri.domain.interactors

import com.pedrenrique.tecnonutri.domain.FeedItem
import com.pedrenrique.tecnonutri.domain.Profile
import com.pedrenrique.tecnonutri.domain.interactors.base.Interactor

interface GetProfileInteractor :
    Interactor<GetProfileInteractor.Params, GetProfileInteractor.Callback> {

    class Params(
        val userId: Int,
        val page: Int = 0,
        val timestamp: Int = 0
    )

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