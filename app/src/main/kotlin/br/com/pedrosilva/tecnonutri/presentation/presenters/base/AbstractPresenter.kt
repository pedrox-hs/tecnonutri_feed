package br.com.pedrosilva.tecnonutri.presentation.presenters.base

import br.com.pedrosilva.tecnonutri.presentation.ui.BaseView
import br.com.pedrosilva.tecnonutri.presentation.presenters.error.ErrorData
import com.pedrenrique.tecnonutri.domain.executor.Executor
import com.pedrenrique.tecnonutri.domain.executor.MainThread

/**
 * This is a base class for all presenters which are communicating with interactors. This base class will hold a
 * reference to the Executor and MainThread objects that are needed for running interactors in a background thread.
 */
abstract class AbstractPresenter<View : BaseView>(
    override var view: View?,
    protected var executor: Executor,
    protected var mainThread: MainThread
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