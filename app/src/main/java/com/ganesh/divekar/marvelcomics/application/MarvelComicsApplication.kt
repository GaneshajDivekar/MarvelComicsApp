package com.ganesh.divekar.marvelcomics.application

import com.ganesh.divekar.feature_home.di.DaggerMarvelComicsAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class MarvelComicsApplication : DaggerApplication() {


    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerMarvelComicsAppComponent.builder().application(this).build()
    }
}