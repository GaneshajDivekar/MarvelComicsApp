package com.ganesh.divekar.feature_home.utils

import com.ganesh.divekar.remotedata.api.ComicsService
import com.ganesh.divekar.remotedata.models.Comic
import com.ganesh.divekar.remotedata.models.DataWrapper
import io.reactivex.Single


class FakeComicsService : ComicsService {
    override fun getComicsListStartsWithTitle(
        md5Digest: String,
        timestamp: Long,
        offset: Int?,
        limit: Int?,
        titleStartsWith: String
    ): Single<DataWrapper<List<Comic>>> {
        return Single.just(dataWrapper)
    }
}