package br.com.pedrosilva.tecnonutri.threading

import android.os.Handler
import android.os.Looper
import com.pedrenrique.tecnonutri.domain.executor.MainThread

/**
 * This class makes sure that the runnable we provide will be run on the main UI thread.
 */
class MainThreadImpl private constructor() : MainThread {

    private val mHandler = Handler(Looper.getMainLooper())

    override fun post(runnable: Runnable) {
        mHandler.post(runnable)
    }

    companion object {

        private var sMainThread: MainThread? = null

        val instance: MainThread
            get() {
                if (sMainThread == null) {
                    sMainThread = MainThreadImpl()
                }
                return sMainThread!!
            }
    }
}