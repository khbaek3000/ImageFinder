package com.brianbaek.imagefinder.di

import android.app.Application
import android.content.Context
import com.brianbaek.imagefinder.di.module.NetworkModule
import com.brianbaek.imagefinder.di.module.ViewModelModule
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class, NetworkModule::class])
class ApplicationModule {
    @Provides
    @Singleton
    fun provideContext(application: Application) : Context {
        return application;
    }
}