package com.ganesh.divekar.remotedata.api

import com.ganesh.divekar.remotedata.models.Comic
import com.ganesh.divekar.remotedata.models.DataWrapper
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

const val HASH = "hash"
const val TIMESTAMP = "ts"
const val OFFSET = "offset"
const val LIMIT = "limit"
const val TITLE = "titleStartsWith"

interface ComicsService {
    @GET("comics")
    fun getComicsListStartsWithTitle(
        @Query(HASH) md5Digest: String,
        @Query(TIMESTAMP) timestamp: Long,
        @Query(OFFSET) offset: Int?,
        @Query(LIMIT) limit: Int?
    ): Single<DataWrapper<List<Comic>>>
}