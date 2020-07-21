package br.com.pedrosilva.tecnonutri.presentation.presenters.base

import com.pedrenrique.tecnonutri.domain.executor.Executor
import com.pedrenrique.tecnonutri.domain.executor.MainThread

/**
 * This is a base class for all presenters which are communicating with interactors. This base class will hold a
 * reference to the Executor and MainThread objects that are needed for running interactors in a background thread.
 */
abstract class AbstractPresenter(
    protected var executor: Executor,
    protected var mainThread: MainThread
)