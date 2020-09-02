package com.brianbaek.imagefinder.di

import android.app.Application
import com.brianbaek.imagefinder.FinderApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    ActivityBuilder::class,
    ApplicationModule::class])
interface AppComponent : AndroidInjector<FinderApplication> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application) : AppComponent.Builder
        fun build() : AppComponent
    }
}