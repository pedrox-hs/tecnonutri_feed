package br.com.pedrosilva.tecnonutri.presentation.ui.listeners

import android.os.SystemClock
import android.view.View

abstract class SingleOnClickListener : View.OnClickListener {

    companion object {
        private const val CLICK_INTERVAL_TIME = 500L
        private var lastClickTime = 0L
    }

    override fun onClick(view: View) {
        if (SystemClock.elapsedRealtime() - lastClickTime < CLICK_INTERVAL_TIME) {
            return
        }
        lastClickTime = SystemClock.elapsedRealtime()
        onItemClick(view)
    }

    abstract fun onItemClick(view: View)
}

fun View.setSingleOnClickListener(listener: (View) -> Unit) {
    setOnClickListener(object : SingleOnClickListener() {
        override fun onItemClick(view: View) {
            listener(view)
        }
    })
}