package com.king.weather.di.component

import com.king.frame.mvvmframe.di.component.AppComponent
import com.king.frame.mvvmframe.di.scope.ApplicationScope
import com.king.weather.di.module.ApplicationModule
import dagger.Component
import com.king.weather.App



/**
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
@ApplicationScope
@Component(dependencies = [AppComponent::class],modules = [ApplicationModule::class])
interface ApplicationComponent {

    fun inject(app: App)
}