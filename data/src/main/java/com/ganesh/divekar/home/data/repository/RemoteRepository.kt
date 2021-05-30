package com.ganesh.divekar.home.data.repository

import com.ganesh.divekar.home.domain.entities.ComicsEntity
import io.reactivex.Single

interface RemoteRepository {
    fun getComicsList(
        offset: Int, limit: Int
    ): Single<List<ComicsEntity>>

}