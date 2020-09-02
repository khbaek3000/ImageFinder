package com.brianbaek.imagefinder.di

import com.brianbaek.imagefinder.activity.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {
    @ContributesAndroidInjector
    abstract fun mainActivity(): MainActivity
}