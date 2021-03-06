package com.pedrenrique.tecnonutri.data.repositories

import com.pedrenrique.tecnonutri.data.converters.asDTO
import com.pedrenrique.tecnonutri.data.datasource.RealmManager
import com.pedrenrique.tecnonutri.data.entities.FeedResponse
import com.pedrenrique.tecnonutri.data.entities.LikedFeedItem
import com.pedrenrique.tecnonutri.data.ext.getBodyOrThrow
import com.pedrenrique.tecnonutri.data.net.services.FeedService
import com.pedrenrique.tecnonutri.domain.FeedItem
import com.pedrenrique.tecnonutri.domain.repositories.FeedRepository
import com.pedrenrique.tecnonutri.domain.repositories.FeedRepository.FeedCallback
import com.pedrenrique.tecnonutri.domain.repositories.FeedRepository.FeedItemCallback
import retrofit2.Call
import javax.inject.Inject

internal class FeedRepositoryImpl @Inject constructor(
    private val manager: RealmManager,
    private val apiService: FeedService,
) : FeedRepository {

    override fun get(feedHash: String, callback: FeedItemCallback) {
        try {
            val feedItem = apiService.item(feedHash).getBodyOrThrow()
                .item.asDTO()
                .let { it.copy(isLiked = isLiked(it.id)) }
            callback.onSuccess(feedItem)
        } catch (e: Exception) {
            callback.onError(e)
        }
    }

    override fun firstList(callback: FeedCallback) {
        apiService.list().handleResponse(callback)
    }

    override fun loadMore(page: Int, timestamp: Int, callback: FeedCallback) {
        apiService.paginate(page, timestamp).handleResponse(callback)
    }

    private fun Call<FeedResponse>.handleResponse(callback: FeedCallback) {
        try {
            val response = getBodyOrThrow()
            val feedItems = checkLikedFeeds(response.items.asDTO())
            callback.onSuccess(feedItems, response.page, response.timestamp)
        } catch (e: Exception) {
            callback.onError(e)
        }
    }

    override fun likeItem(feedHash: String) {
        manager.transactionAsync {
            copyToRealmOrUpdate(LikedFeedItem(feedHash))
        }
    }

    override fun dislikeItem(feedHash: String) {
        manager.transactionAsync {
            where(LikedFeedItem::class.java)
                .equalTo("feedHash", feedHash)
                .findAll()
                .deleteAllFromRealm()
        }
    }

    private fun isLiked(feedHash: String) =
        manager.execute {
            val likedFeedItem = where(LikedFeedItem::class.java)
                .equalTo("feedHash", feedHash)
                .findFirst()
            likedFeedItem != null
        }

    private fun checkLikedFeeds(items: List<FeedItem>): List<FeedItem> {
        if (items.isEmpty()) return items
        val feedHashes = items.map { it.id }.toTypedArray()
        return manager.execute {
            val likedFeedItems = where(LikedFeedItem::class.java)
                .`in`("feedHash", feedHashes)
                .findAll()
                .let { results -> copyFromRealm(results).map { it.feedHash } }

            items.map { it.copy(isLiked = likedFeedItems.contains(it.id)) }
        }
    }
}