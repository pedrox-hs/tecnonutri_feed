package br.com.pedrosilva.tecnonutri.presentation.ui.activities

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import br.com.pedrosilva.tecnonutri.R
import br.com.pedrosilva.tecnonutri.presentation.ui.HasPresenter
import br.com.pedrosilva.tecnonutri.presentation.presenters.base.BasePresenter
import br.com.pedrosilva.tecnonutri.presentation.presenters.error.ErrorData
import br.com.pedrosilva.tecnonutri.presentation.ui.listeners.setSingleOnClickListener
import br.com.pedrosilva.tecnonutri.presentation.ui.BaseView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.progress.ll_error
import kotlinx.android.synthetic.main.progress.progress_bar
import kotlinx.android.synthetic.main.progress.tv_error_message

abstract class BaseActivity : AppCompatActivity(),
    BaseView {

    private val presenter: BasePresenter<*>?
        get() = (this as? HasPresenter)?.presenter

    private var hasProgress = false

    private val contentView by lazy {
        findViewById<View>(android.R.id.content)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        presenter?.create()
    }

    override fun onResume() {
        super.onResume()
        presenter?.resume()
    }

    override fun onPause() {
        super.onPause()
        presenter?.pause()
    }

    override fun onStop() {
        super.onStop()
        presenter?.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.destroy()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    override fun showError(error: ErrorData) {
        Snackbar.make(contentView, error.message, Snackbar.LENGTH_LONG).show()
        if (hasProgress) {
            tv_error_message.text = error.message
            ll_error.visibility = View.VISIBLE
            progress_bar.visibility = View.GONE
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
            ll_error.setSingleOnClickListener {
                ll_error.visibility = View.INVISIBLE
                progress_bar.visibility = View.VISIBLE
                reloadAll()
            }
        }
    }

    open fun reloadAll() {}
}