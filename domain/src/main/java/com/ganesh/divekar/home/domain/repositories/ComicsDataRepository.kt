package com.ganesh.divekar.home.domain.repositories

import com.ganesh.divekar.home.domain.usecases.GetComicsListAction
import com.ganesh.divekar.home.domain.usecases.GetComicsListAction.GetComicsListActionResult

interface ComicsDataRepository {

    fun getComicsList(
        query: GetComicsListAction.Params
    ): GetComicsListActionResult

    fun clean()

}