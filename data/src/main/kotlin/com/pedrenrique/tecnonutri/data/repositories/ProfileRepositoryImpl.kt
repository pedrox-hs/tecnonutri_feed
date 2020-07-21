package com.pedrenrique.tecnonutri.data.repositories

import com.pedrenrique.tecnonutri.data.converters.asDTO
import com.pedrenrique.tecnonutri.data.entities.ProfileResponse
import com.pedrenrique.tecnonutri.data.ext.getBodyOrThrow
import com.pedrenrique.tecnonutri.data.net.ApiServiceGenerator
import com.pedrenrique.tecnonutri.data.net.services.ProfileService
import com.pedrenrique.tecnonutri.domain.repositories.ProfileRepository
import com.pedrenrique.tecnonutri.domain.repositories.ProfileRepository.ProfileCallback
import retrofit2.Call

class ProfileRepositoryImpl : ProfileRepository {

    private val apiService by lazy {
        ApiServiceGenerator.getService<ProfileService>()
    }

    override operator fun get(userId: Int, callback: ProfileCallback) {
        apiService.profile(userId).handleResponse(callback)
    }

    override fun loadMorePosts(userId: Int, page: Int, timestamp: Int, callback: ProfileCallback) {
        apiService.posts(userId, page, timestamp).handleResponse(callback)
    }

    private fun Call<ProfileResponse>.handleResponse(callback: ProfileCallback) {
        try {
            val response = getBodyOrThrow()
            val (page, profile, feed, timestamp) = response
            callback.onSuccess(profile.asDTO(), feed.asDTO(), page, timestamp)
        } catch (e: Exception) {
            callback.onError(e)
        }
    }
}