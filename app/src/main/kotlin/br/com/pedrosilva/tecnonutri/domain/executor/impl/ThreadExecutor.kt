package br.com.pedrosilva.tecnonutri.domain.executor.impl

import br.com.pedrosilva.tecnonutri.domain.executor.Executor
import br.com.pedrosilva.tecnonutri.domain.interactors.base.AbstractInteractor
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * This singleton class will make sure that each interactor operation gets a background thread.
 *
 *
 */
class ThreadExecutor private constructor() : Executor {

    private val mThreadPoolExecutor = ThreadPoolExecutor(
        CORE_POOL_SIZE,
        MAX_POOL_SIZE,
        KEEP_ALIVE_TIME,
        TIME_UNIT,
        WORK_QUEUE
    )

    override fun execute(interactor: AbstractInteractor) {
        mThreadPoolExecutor.submit {
            // run the main logic
//                Looper.prepare();
            interactor.run()

            // mark it as finished
            interactor.onFinished()
            //                Looper.loop();
        }
    }

    companion object {
        // This is a singleton
        @Volatile
        private var sThreadExecutor: ThreadExecutor? = null
        private const val CORE_POOL_SIZE = 3
        private const val MAX_POOL_SIZE = 5
        private const val KEEP_ALIVE_TIME = 120L
        private val TIME_UNIT = TimeUnit.SECONDS
        private val WORK_QUEUE: BlockingQueue<Runnable> = LinkedBlockingQueue()

        /**
         * Returns a singleton instance of this executor. If the executor is not initialized then it initializes it and returns
         * the instance.
         */
        val instance: Executor
            get() {
                if (sThreadExecutor == null) {
                    sThreadExecutor = ThreadExecutor()
                }
                return sThreadExecutor!!
            }
    }
}