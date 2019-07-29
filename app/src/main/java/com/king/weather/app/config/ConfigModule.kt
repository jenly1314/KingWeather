package com.king.weather.app.config

import android.content.Context
import com.king.frame.mvvmframe.config.FrameConfigModule
import com.king.frame.mvvmframe.di.module.ConfigModule
import com.king.weather.app.Constants

/**
 * MVVMFrame 全局配置
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
class ConfigModule : FrameConfigModule(){

    override fun applyOptions(context: Context?, builder: ConfigModule.Builder?) {
        builder?.baseUrl(Constants.BASE_URL)
    }

}