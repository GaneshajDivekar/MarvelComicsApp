package com.ganesh.divekar.feature_home.di

import com.ganesh.divekar.home.data.repository.ComicsDataRepositoryImpl
import com.ganesh.divekar.home.domain.repositories.ComicsDataRepository
import dagger.Binds
import dagger.Module

@Module
abstract class DataModule {
    @Binds
    abstract fun bindsRepository(
        repoImpl: ComicsDataRepositoryImpl
    ): ComicsDataRepository

}