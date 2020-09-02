package com.brianbaek.imagefinder

import com.brianbaek.imagefinder.di.AppComponent
import com.brianbaek.imagefinder.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class FinderApplication : DaggerApplication () {
    companion object{
        lateinit var appComponent: AppComponent

    }
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        appComponent =  DaggerAppComponent.builder().application(this).build()
        return appComponent;
    }
}