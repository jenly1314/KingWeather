package com.king.weather.binding

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.king.weather.util.WeatherUtils

/**
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */

@BindingAdapter(value = ["bind:temperature"])
fun TextView.temperatureFormat(temperature: String?){
    temperature?.run {
        text = "${temperature}℃"
    } ?: run {
        text = ""
    }

}

@BindingAdapter(value = ["bind:date"])
fun TextView.dateFormat(date: String?){
    date?.run {
        text = "${WeatherUtils.dateFormat(date)} 发布"
    } ?: run {
        text = ""
    }

}

@BindingAdapter(value = ["bind:week"])
fun TextView.weekFormat(week: String?){
    week?.run {
        text = WeatherUtils.weekFormat(context, week)
    } ?: run {
        text = ""
    }

}

@BindingAdapter(value = ["bind:windDirection","bind:windPower"] , requireAll = true)
fun TextView.windFormat(windDirection: String?,windPower: String?){
    windDirection?.let {
        windPower?.let {
            text = "${windDirection}风\t\t${windPower}级"
            return
        }
    }

    text = ""
}

@BindingAdapter(value = ["bind:humidity"])
fun TextView.win1dFormat(humidity: String?){
    humidity?.run {
        text = "湿度\t\t${humidity}%"
    } ?: run {
        text = ""
    }
}