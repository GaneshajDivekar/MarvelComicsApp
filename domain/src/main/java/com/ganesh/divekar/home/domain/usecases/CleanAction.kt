package com.ganesh.divekar.home.domain.usecases

import com.ganesh.divekar.home.domain.repositories.ComicsDataRepository
import javax.inject.Inject

class CleanAction @Inject constructor(
    private val comicsDataRepository: ComicsDataRepository
) {
    fun execute() {
        comicsDataRepository.clean()
    }
}