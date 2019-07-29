package com.king.weather.di.module

import com.king.frame.mvvmframe.di.component.BaseActivitySubcomponent
import com.king.weather.app.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
@Module(subcomponents = [BaseActivitySubcomponent::class])
abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity
}