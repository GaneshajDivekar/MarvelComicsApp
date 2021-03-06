package com.ganesh.divekar.feature_home.di

import com.ganesh.divekar.home.data.repository.RemoteRepository
import com.ganesh.divekar.marvelcomics.BuildConfig
import com.ganesh.divekar.remotedata.api.ComicsService
import com.ganesh.divekar.remotedata.datasource.NetworkDataSource
import com.ganesh.divekar.remotedata.http.ApiKeyInterceptor
import com.ganesh.divekar.remotedata.qualifiers.PrivateKey
import com.ganesh.divekar.remotedata.qualifiers.PublicKey
import dagger.Binds
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module(includes = [RemoteModule.Binders::class])
class RemoteModule {

    @Module
    interface Binders {

        @Binds
        fun bindsRemoteSource(
            remoteDataSourceImpl: NetworkDataSource
        ): RemoteRepository
    }

    @Provides
    @PublicKey
    fun providePublicKey() = BuildConfig.PUBLIC_KEY

    @Provides
    @PrivateKey
    fun providePrivateKey() = BuildConfig.PRIVATE_KEY


    @Provides
    fun provideComicsService(retrofit: Retrofit): ComicsService =
        retrofit.create(ComicsService::class.java)


    @Provides
    fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(BuildConfig.BASE_URL)
            .build()

    @Provides
    @Singleton
    fun provideHttpClient(apiKeyInterceptor: ApiKeyInterceptor): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        val level = getInterceptorLevel()
        httpLoggingInterceptor.level = level
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(apiKeyInterceptor)
            .cache(null)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS).build()
    }

    private fun getInterceptorLevel(): HttpLoggingInterceptor.Level? {
        return if (BuildConfig.DEBUG)
            HttpLoggingInterceptor.Level.BODY
        else
            HttpLoggingInterceptor.Level.NONE
    }

}