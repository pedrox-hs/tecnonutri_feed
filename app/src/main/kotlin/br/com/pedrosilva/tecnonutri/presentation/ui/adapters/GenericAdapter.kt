package br.com.pedrosilva.tecnonutri.presentation.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.TextView
import br.com.pedrosilva.tecnonutri.R
import br.com.pedrosilva.tecnonutri.presentation.ui.listeners.setSingleOnClickListener

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

        private val progressBar: View = v.findViewById(R.id.progress_bar)
        private val llError: View = v.findViewById(R.id.ll_error)
        private val tvErrorMessage: TextView = v.findViewById(R.id.tv_error_message)

        @JvmOverloads
        fun setup(
            heightForOne: Int = MATCH_PARENT,
            heightForMore: Int = WRAP_CONTENT
        ) {
            val vhLayoutParams = itemView.layoutParams
            if (itemCount == 1) {
                vhLayoutParams.height = heightForOne
            } else {
                vhLayoutParams.height = heightForMore
            }
            itemView.layoutParams = vhLayoutParams
            llError.setSingleOnClickListener {
                if (retryClickListener != null) {
                    showError = false
                    msgError()
                    retryClickListener?.invoke()
                }
            }
            msgError()
        }

        private fun msgError() {
            if (showError) {
                progressBar.visibility = View.GONE
                llError.visibility = View.VISIBLE
                tvErrorMessage.text = errorMessage
            } else {
                progressBar.visibility = View.VISIBLE
                llError.visibility = View.INVISIBLE
            }
        }
    }
}