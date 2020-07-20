package br.com.pedrosilva.tecnonutri.data.repositories

import br.com.pedrosilva.tecnonutri.data.converters.asDTO
import br.com.pedrosilva.tecnonutri.data.entities.FeedResponse
import br.com.pedrosilva.tecnonutri.data.entities.LikedFeedItem
import br.com.pedrosilva.tecnonutri.data.ext.getBodyOrThrow
import br.com.pedrosilva.tecnonutri.data.net.ApiServiceGenerator
import br.com.pedrosilva.tecnonutri.data.net.services.FeedService
import br.com.pedrosilva.tecnonutri.domain.entities.FeedItem
import br.com.pedrosilva.tecnonutri.domain.repositories.FeedRepository
import br.com.pedrosilva.tecnonutri.domain.repositories.FeedRepository.FeedCallback
import br.com.pedrosilva.tecnonutri.domain.repositories.FeedRepository.FeedItemCallback
import io.realm.Realm
import retrofit2.Call

class FeedRepositoryImpl : FeedRepository {

    private val apiService by lazy {
        ApiServiceGenerator.getService<FeedService>()
    }

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
        Realm.getDefaultInstance().use {
            it.executeTransactionAsync { realm ->
                realm.copyToRealmOrUpdate(LikedFeedItem(feedHash))
            }
        }
    }

    override fun dislikeItem(feedHash: String) {
        Realm.getDefaultInstance().use {
            it.executeTransactionAsync { realm ->
                realm.where(LikedFeedItem::class.java)
                    .equalTo("feedHash", feedHash)
                    .findAll()
                    .deleteAllFromRealm()
            }
        }
    }

    override fun isLiked(feedHash: String) =
        Realm.getDefaultInstance().use { realm ->
            val likedFeedItem = realm.where(LikedFeedItem::class.java)
                .equalTo("feedHash", feedHash)
                .findFirst()
            likedFeedItem != null
        }

    private fun checkLikedFeeds(items: List<FeedItem>): List<FeedItem> {
        if (items.isEmpty()) return items
        val feedHashes = items.map { it.id }.toTypedArray()
        return Realm.getDefaultInstance().use { realm ->
            val likedFeedItems = realm.where(LikedFeedItem::class.java)
                .`in`("feedHash", feedHashes)
                .findAll()
                .let { results -> realm.copyFromRealm(results).map { it.feedHash } }

            items.map { it.copy(isLiked = likedFeedItems.contains(it.id)) }
        }
    }
}