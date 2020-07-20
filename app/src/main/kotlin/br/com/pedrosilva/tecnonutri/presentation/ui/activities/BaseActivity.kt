package br.com.pedrosilva.tecnonutri.presentation.ui.activities

import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import br.com.pedrosilva.tecnonutri.R
import br.com.pedrosilva.tecnonutri.presentation.ui.BaseView
import br.com.pedrosilva.tecnonutri.presentation.ui.listeners.SingleOnClickListener
import br.com.pedrosilva.tecnonutri.presentation.ui.listeners.setSingleOnClickListener

abstract class BaseActivity : AppCompatActivity(), BaseView {

    private lateinit var progressBar: View

    private lateinit var llError: View

    private var tvErrorMessage: TextView? = null

    private var hasProgress = false

    private val contentView by lazy {
        findViewById<View>(android.R.id.content)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    override fun showError(message: String) {
        Snackbar.make(contentView, message, Snackbar.LENGTH_LONG).show()
        if (hasProgress) {
            tvErrorMessage!!.text = message
            llError.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }
    }

    override fun showProgress() {
        val hideOnProgress =
            contentView.findViewWithTag<View>("hide_on_progress")
        if (hideOnProgress != null) {
            hideOnProgress.visibility = View.GONE
        }
        val showOnProgress =
            contentView.findViewWithTag<View>("show_on_progress")
        if (showOnProgress != null) {
            showOnProgress.visibility = View.VISIBLE
        }
    }

    override fun hideProgress() {
        val hideOnProgress =
            contentView.findViewWithTag<View>("hide_on_progress")
        if (hideOnProgress != null) {
            hideOnProgress.visibility = View.VISIBLE
        }
        val showOnProgress =
            contentView.findViewWithTag<View>("show_on_progress")
        if (showOnProgress != null) {
            showOnProgress.visibility = View.GONE
        }
    }

    protected fun bindProgress() {
        val v = findViewById<View>(R.id.rl_progress)
        if (v != null) {
            hasProgress = true
            progressBar = v.findViewById(R.id.progress_bar)
            llError = v.findViewById(R.id.ll_error)
            tvErrorMessage = v.findViewById<View>(R.id.tv_error_message) as TextView
            llError.setSingleOnClickListener {
                llError.visibility = View.INVISIBLE
                progressBar.visibility = View.VISIBLE
                reloadAll()
            }
        }
    }

    open fun reloadAll() {}
}