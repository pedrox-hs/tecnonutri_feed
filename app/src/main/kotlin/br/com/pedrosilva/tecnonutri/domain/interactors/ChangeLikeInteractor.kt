package br.com.pedrosilva.tecnonutri.domain.interactors

interface ChangeLikeInteractor {

    interface Callback {
        fun onChange(feedHash: String, liked: Boolean)
    }
}