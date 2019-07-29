package com.king.weather.api

import com.king.weather.bean.City
import retrofit2.Call
import retrofit2.http.GET

/**
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
interface ApiService {

    /**
     * 获取热门城市列表
     */
    @GET("api/city/hotCities.json")
    fun getHotCities(): Call<MutableList<City>>

}