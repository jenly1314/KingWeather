package com.king.weather.util

import android.content.Context
import com.king.base.util.TimeUtils
import com.king.weather.R

/**
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
object WeatherUtils {

    fun weekFormat(context: Context,week: String): String{
        when (week) {
            "1" -> return context.getString(R.string.week_monday)
            "2" -> return context.getString(R.string.week_tuesday)
            "3" -> return context.getString(R.string.week_wednesday)
            "4" -> return context.getString(R.string.week_thursday)
            "5" -> return context.getString(R.string.week_friday)
            "6" -> return context.getString(R.string.week_saturday)
            "7" -> return context.getString(R.string.week_sunday)

        }
        return ""
    }

    fun dateFormat(date: String): String{
        return TimeUtils.formatDate(date,TimeUtils.FORMAT_Y_TO_S_EN,TimeUtils.FORMAT_Y_TO_M_EN)
    }
}