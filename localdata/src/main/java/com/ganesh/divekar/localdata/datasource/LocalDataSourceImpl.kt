package com.ganesh.divekar.localdata.datasource

import androidx.paging.DataSource
import com.ganesh.divekar.home.data.repository.LocalRepository
import com.ganesh.divekar.home.domain.entities.ComicsEntity
import com.ganesh.divekar.home.domain.usecases.GetComicsListAction
import com.ganesh.divekar.localdata.db.ComicsListDAO
import com.ganesh.divekar.localdata.mapper.ComicsLocalMapper
import io.reactivex.Completable
import java.util.*
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val comicsLocalMapper: ComicsLocalMapper,
    private val comicsListDAO: ComicsListDAO
) : LocalRepository {
    override fun insert(
        comicsEntityList: List<ComicsEntity>
    ): Completable {
        return comicsListDAO.insert(comicsEntityList.map { comicsLocalMapper.to(it) })
    }

    override fun getComicsListDatasourceFactory(param: GetComicsListAction.Params): DataSource.Factory<Int, ComicsEntity> {
        return comicsListDAO.getComicsList(
            "%${param.searchKey.toUpperCase(Locale.getDefault()).replace(' ', '%')}%"
        ).map { comicsLocalMapper.from(it) }
    }
}
