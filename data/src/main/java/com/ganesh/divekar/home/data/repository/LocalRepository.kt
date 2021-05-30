package com.ganesh.divekar.home.data.repository

import androidx.paging.DataSource
import com.ganesh.divekar.home.domain.entities.ComicsEntity
import com.ganesh.divekar.home.domain.usecases.GetComicsListAction
import io.reactivex.Completable


interface LocalRepository {
    fun insert(
        comicsEntityList: List<ComicsEntity>
    ): Completable

    fun getComicsListDatasourceFactory(param: GetComicsListAction.Params): DataSource.Factory<Int, ComicsEntity>

}