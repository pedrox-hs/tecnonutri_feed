package com.pedrenrique.tecnonutri.domain.interactors

interface ChangeLikeInteractor {

    interface Callback {
        fun onChange(feedHash: String, liked: Boolean)
    }
}