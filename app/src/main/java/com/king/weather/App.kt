package com.king.weather

import android.content.Context
import com.king.frame.mvvmframe.base.BaseApplication
import com.king.weather.app.Constants
import com.king.weather.di.component.DaggerApplicationComponent
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import timber.log.Timber

/**
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
class App : BaseApplication(){

    override fun attachBaseContext(base: Context?) {
        //初始化打印日志
        var formatStrategy = PrettyFormatStrategy.newBuilder()
            .methodOffset(5)
            .tag(Constants.TAG)
            .build()

        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))

        Timber.plant(object : Timber.DebugTree() {
            override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                if (BuildConfig.DEBUG) {
                    Logger.log(priority, tag, message, t)
                }
            }
        })

        super.attachBaseContext(base)

    }

    override fun onCreate() {
        super.onCreate()
        DaggerApplicationComponent.builder()
            .appComponent(appComponent)
            .build()
            .inject(this)
    }

}