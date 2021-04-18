package com.pedrenrique.tecnonutri.domain.executor.impl

import com.pedrenrique.tecnonutri.domain.executor.Executor
import com.pedrenrique.tecnonutri.domain.interactors.base.AbstractInteractor
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * This singleton class will make sure that each interactor operation gets a background thread.
 *
 *
 */
@Singleton
class ThreadExecutor @Inject constructor() : Executor {

    private val mThreadPoolExecutor = ThreadPoolExecutor(
        CORE_POOL_SIZE,
        MAX_POOL_SIZE,
        KEEP_ALIVE_TIME,
        TIME_UNIT,
        WORK_QUEUE
    )

    override fun <Params, Callback> execute(
        interactor: AbstractInteractor<Params, Callback>,
        params: Params,
        callback: Callback
    ) {
        mThreadPoolExecutor.submit {
            // run the main logic
//                Looper.prepare();
            interactor.run(params, callback)

            // mark it as finished
            interactor.onFinished()
            //                Looper.loop();
        }
    }

    companion object {
        private const val CORE_POOL_SIZE = 3
        private const val MAX_POOL_SIZE = 5
        private const val KEEP_ALIVE_TIME = 120L
        private val TIME_UNIT = TimeUnit.SECONDS
        private val WORK_QUEUE: BlockingQueue<Runnable> = LinkedBlockingQueue()
    }
}