package com.pedrenrique.tecnonutri.data.ext

import retrofit2.Call
import retrofit2.HttpException

internal fun <T> Call<T>.getBodyOrThrow(): T {
    val response = execute()
    return response.takeIf { it.isSuccessful }?.body()
        ?: throw HttpException(response)
}