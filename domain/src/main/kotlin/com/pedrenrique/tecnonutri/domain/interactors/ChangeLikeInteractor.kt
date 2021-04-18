package com.pedrenrique.tecnonutri.domain.interactors

import com.pedrenrique.tecnonutri.domain.interactors.base.Interactor

interface ChangeLikeInteractor :
    Interactor<ChangeLikeInteractor.Params, ChangeLikeInteractor.Callback> {

    class Params(
        val feedHash: String,
        val liked: Boolean,
    )

    interface Callback {
        fun onChange(feedHash: String, liked: Boolean)
    }
}