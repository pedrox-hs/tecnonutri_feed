package br.com.pedrosilva.tecnonutri.domain.interactors.base

import br.com.pedrosilva.tecnonutri.domain.executor.Executor
import br.com.pedrosilva.tecnonutri.domain.executor.MainThread

/**
 * Created by dmilicic on 8/4/15.
 *
 *
 * This abstract class implements some common methods for all interactors. Cancelling an interactor, check if its running
 * and finishing an interactor has mostly the same code throughout so that is why this class was created. Field methods
 * are declared volatile as we might use these methods from different threads (mainly from UI).
 *
 *
 * For example, when an activity is getting destroyed then we should probably cancel an interactor
 * but the request will come from the UI thread unless the request was specifically assigned to a background thread.
 */
abstract class AbstractInteractor(
    private var threadExecutor: Executor,
    protected var mainThread: MainThread
) : Interactor {

    @Volatile
    protected var isCanceled = false

    @Volatile
    var isRunning = false
        protected set

    /**
     * This method contains the actual business logic of the interactor. It SHOULD NOT BE USED DIRECTLY but, instead, a
     * developer should call the execute() method of an interactor to make sure the operation is done on a background thread.
     *
     *
     * This method should only be called directly while doing unit/integration tests. That is the only reason it is declared
     * public as to help with easier testing.
     */
    abstract fun run()

    fun cancel() {
        isCanceled = true
        isRunning = false
    }

    fun onFinished() {
        isRunning = false
        isCanceled = false
    }

    override fun execute() {

        // mark this interactor as running
        isRunning = true

        // start running this interactor in a background thread
        threadExecutor.execute(this)
    }
}