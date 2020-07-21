package br.com.pedrosilva.tecnonutri.presentation.ui.adapters

import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.recyclerview.widget.RecyclerView
import br.com.pedrosilva.tecnonutri.presentation.ui.listeners.setSingleOnClickListener
import kotlinx.android.synthetic.main.progress.view.ll_error
import kotlinx.android.synthetic.main.progress.view.progress_bar
import kotlinx.android.synthetic.main.progress.view.tv_error_message

typealias RetryClickListener = () -> Unit

abstract class GenericAdapter<T, A : RecyclerView.ViewHolder>(
    private val hasLoader: Boolean = false
) : RecyclerView.Adapter<A>() {

    private val mutableItems = mutableSetOf<T>()

    var items: List<T>
        get() = mutableItems.toList()
        set(value) {
            mutableItems.clear()
            mutableItems.addAll(sortItems(value))
            isListEnded = false
            notifyDataSetChanged()
        }

    var isListEnded = false
        private set

    private var retryClickListener: RetryClickListener? = null
    private var errorMessage: String? = null

    private var showError = false

    companion object {
        internal const val VIEW_TYPE_ITEM = 0
        internal const val VIEW_TYPE_LOADER = 1
    }

    override fun getItemViewType(position: Int): Int =
        if (hasLoader && position >= items.size) VIEW_TYPE_LOADER
        else VIEW_TYPE_ITEM

    fun appendItems(items: List<T>) {
        isListEnded = false
        val sortedItems = sortItems(items)
        val positionStart = itemCount
        var itemCount = 0
        for (item in sortedItems) {
            if (mutableItems.add(item)) {
                itemCount++
            }
        }
        itemCount = getItemCount()
        if (positionStart != itemCount) {
            notifyItemChanged(positionStart - 1)
            notifyItemRangeInserted(positionStart, itemCount)
        }
    }

    protected open fun sortItems(items: List<T>): List<T> = items

    override fun getItemCount(): Int =
        items.size + if (hasLoader && !isListEnded) 1 else 0

    fun getItem(pos: Int): T = items[pos]

    fun setRetryClickListener(retryClickListener: RetryClickListener?) {
        this.retryClickListener = retryClickListener
    }

    fun notifyEndList() {
        isListEnded = true
        notifyItemRemoved(items.size)
    }

    fun notifyError(message: String?) {
        if (!isListEnded) {
            showError = true
            errorMessage = message
            notifyItemChanged(items.size)
        }
    }

    internal inner class ViewHolderLoader(v: View) : RecyclerView.ViewHolder(v) {

        @JvmOverloads
        fun setup(
            heightForOne: Int = MATCH_PARENT,
            heightForMore: Int = WRAP_CONTENT
        ) = itemView.run {
            val vhLayoutParams = layoutParams
            if (itemCount == 1) {
                vhLayoutParams.height = heightForOne
            } else {
                vhLayoutParams.height = heightForMore
            }
            layoutParams = vhLayoutParams
            tv_error_message.setSingleOnClickListener {
                if (retryClickListener != null) {
                    showError = false
                    msgError()
                    retryClickListener?.invoke()
                }
            }
            msgError()
        }

        private fun msgError() = itemView.run {
            if (showError) {
                progress_bar.visibility = View.GONE
                ll_error.visibility = View.VISIBLE
                tv_error_message.text = errorMessage
            } else {
                progress_bar.visibility = View.VISIBLE
                ll_error.visibility = View.INVISIBLE
            }
        }
    }
}