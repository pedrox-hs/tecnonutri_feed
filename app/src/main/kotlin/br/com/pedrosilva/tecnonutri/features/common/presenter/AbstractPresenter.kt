package br.com.pedrosilva.tecnonutri.features.common.presenter

import br.com.pedrosilva.tecnonutri.features.common.view.BaseView
import br.com.pedrosilva.tecnonutri.features.common.error.ErrorData
import com.pedrenrique.tecnonutri.domain.executor.Executor
import com.pedrenrique.tecnonutri.domain.executor.MainThread

/**
 * This is a base class for all presenters which are communicating with interactors. This base class will hold a
 * reference to the Executor and MainThread objects that are needed for running interactors in a background thread.
 */
abstract class AbstractPresenter<View : BaseView>(
    override var view: View?
) : BasePresenter<View> {

    override fun create() {}

    override fun resume() {}

    override fun pause() {}

    override fun stop() {}

    override fun destroy() {
        view = null
    }

    override fun onError(error: ErrorData) {
        view?.showError(error)
    }
}