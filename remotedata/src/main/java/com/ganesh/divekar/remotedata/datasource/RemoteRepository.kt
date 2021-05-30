package com.ganesh.divekar.remotedata.datasource

import com.ganesh.divekar.home.data.repository.RemoteRepository
import com.ganesh.divekar.home.domain.entities.ComicsEntity
import com.ganesh.divekar.home.domain.qualifiers.Background
import com.ganesh.divekar.remotedata.api.ComicsService
import com.ganesh.divekar.remotedata.mapper.ComicsRemoteMapper
import com.ganesh.divekar.remotedata.qualifiers.PrivateKey
import com.ganesh.divekar.remotedata.qualifiers.PublicKey
import com.ganesh.divekar.utils.HashGenerator
import io.reactivex.Scheduler
import io.reactivex.Single
import javax.inject.Inject

class NetworkDataSource @Inject constructor(
    private val comicsService: ComicsService,
    private val comicsRemoteMapper: ComicsRemoteMapper,
    private val hashGenerator: HashGenerator,
    @PublicKey private val publicKey: String,
    @PrivateKey private val privateKey: String,
    @Background private val backgroundThread: Scheduler

) : RemoteRepository {
    override fun getComicsList(
        offset: Int,
        limit: Int
    ): Single<List<ComicsEntity>> {
        val timestamp = System.currentTimeMillis()
        val hash = "$timestamp$privateKey$publicKey"
        return  comicsService.getComicsListStartsWithTitle(
            offset = offset*limit,
            limit = limit,
            timestamp = timestamp,
            md5Digest = hashGenerator.buildMD5Digest(hash)
        ).subscribeOn(backgroundThread)
            .map {
                it.data.results.map { comic ->
                    comicsRemoteMapper.from(
                        comic
                    )
                }
            }

    }
}