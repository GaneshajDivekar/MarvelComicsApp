package com.ganesh.divekar.feature_home.di

import android.app.Application
import com.ganesh.divekar.home.data.repository.LocalRepository
import com.ganesh.divekar.localdata.datasource.LocalDataSourceImpl
import com.ganesh.divekar.localdata.db.ComicsDB
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [LocalPersistenceModule.Binders::class])
class LocalPersistenceModule {

    @Module
    interface Binders {

        @Binds
        fun bindsLocalDataSource(
            localDataSourceImpl: LocalDataSourceImpl
        ): LocalRepository

    }

    @Provides
    @Singleton
    fun providesDatabase(
        application: Application
    ) = ComicsDB.getInstance(application.applicationContext)

    @Provides
    @Singleton
    fun providesComicsDAO(
        comicsDB: ComicsDB
    ) = comicsDB.getComicsListDao()


}
