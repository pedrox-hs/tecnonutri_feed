package br.com.pedrosilva.tecnonutri.util

import android.content.Context

object ContextHelper {
    var applicationContext: Context? = null
        private set

    fun init(context: Context) {
        applicationContext = context.applicationContext
    }
}