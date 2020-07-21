package br.com.pedrosilva.tecnonutri.presentation.ui.listeners

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

typealias OnLoadMore = (totalItemsCount: Int, view: RecyclerView) -> Unit

internal class EndlessRecyclerViewScrollListener private constructor(
    private val layoutManager: RecyclerView.LayoutManager,
    private val visibleThreshold: Int,
    // Defines the process for actually loading more data based on page
    private val onLoadMore: OnLoadMore
) : RecyclerView.OnScrollListener() {

    companion object {
        // The minimum amount of items to have below your current scroll position
        // before loading more.
        private const val DEFAULT_VISIBLE_THRESHOLD = 3
    }

    // The total number of items in the dataset after the last load
    private var previousTotalItemCount = 0

    // True if we are still waiting for the last set of data to load.
    private var loading = true

    // Sets the starting page index
    private val startingPageIndex = 0

    constructor(layoutManager: LinearLayoutManager, onLoadMore: OnLoadMore) : this(
        layoutManager,
        DEFAULT_VISIBLE_THRESHOLD,
        onLoadMore
    )

    constructor(layoutManager: GridLayoutManager, onLoadMore: OnLoadMore) :
        this(layoutManager, DEFAULT_VISIBLE_THRESHOLD * layoutManager.spanCount, onLoadMore)

    constructor(layoutManager: StaggeredGridLayoutManager, onLoadMore: OnLoadMore) :
        this(layoutManager, DEFAULT_VISIBLE_THRESHOLD * layoutManager.spanCount, onLoadMore)

    private fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
        var maxSize = 0
        for (i in lastVisibleItemPositions.indices) {
            if (i == 0 || lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i]
            }
        }
        return maxSize
    }

    // This happens many times a second during a scroll, so be wary of the code you place here.
    // We are given a few useful parameters to help us work out if we need to load some more data,
    // but first we check if we are waiting for the previous load to finish.
    override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
        val totalItemCount = layoutManager.itemCount
        val lastVisibleItemPosition = when (layoutManager) {
            is StaggeredGridLayoutManager -> {
                val lastVisibleItemPositions =
                    layoutManager.findLastVisibleItemPositions(null)
                // get maximum element within the list
                getLastVisibleItem(lastVisibleItemPositions)
            }
            is GridLayoutManager -> layoutManager.findLastVisibleItemPosition()
            is LinearLayoutManager -> layoutManager.findLastVisibleItemPosition()
            else -> error("LayoutManager not supported")
        }

        // If the total item count is zero and the previous isn't, assume the
        // list is invalidated and should be reset back to initial state
        if (totalItemCount < previousTotalItemCount) {
            previousTotalItemCount = totalItemCount
            if (totalItemCount == 0) {
                loading = true
            }
        }
        // If it’s still loading, we check to see if the dataset count has
        // changed, if so we conclude it has finished loading and update the current page
        // number and total item count.
        if (loading && totalItemCount > previousTotalItemCount) {
            loading = false
            previousTotalItemCount = totalItemCount
        }

        // If it isn’t currently loading, we check to see if we have breached
        // the visibleThreshold and need to reload more data.
        // If we do need to reload some more data, we execute onLoadMore to fetch the data.
        // threshold should reflect how many total columns there are too
        if (!loading && lastVisibleItemPosition + visibleThreshold > totalItemCount) {
            onLoadMore(totalItemCount, view)
            loading = true
        }
    }

    // Call this method whenever performing new searches
    fun resetState() {
        previousTotalItemCount = 0
        loading = true
    }
}