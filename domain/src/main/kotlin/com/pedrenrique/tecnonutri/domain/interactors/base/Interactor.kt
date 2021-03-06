package com.pedrenrique.tecnonutri.domain.interactors.base

/**
 * This is the main interface of an interactor. Each interactor serves a specific use case.
 */
interface Interactor<Params, Callback> {

    /**
     * This is the main method that starts an interactor. It will make sure that the interactor operation is done on a
     * background thread.
     */
    fun execute(params: Params, callback: Callback)

    fun cancel()
}