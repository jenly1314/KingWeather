package com.king.weather.app.main

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.king.frame.mvvmframe.base.BaseModel
import com.king.frame.mvvmframe.base.DataViewModel
import com.king.frame.mvvmframe.base.livedata.StatusEvent
import com.king.frame.mvvmframe.http.callback.ApiCallback
import com.king.weather.api.ApiService
import com.king.weather.bean.City
import retrofit2.Call
import javax.inject.Inject

/**
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
class MainViewModel @Inject constructor(application: Application, model: BaseModel?) : DataViewModel(application, model){

    var citiesLiveData = MutableLiveData<MutableList<City>>()

    override fun onCreate() {
        super.onCreate()
        getHotCities()
    }

    /**
     * 获取城市列表
     */
    private fun getHotCities(){
        updateStatus(StatusEvent.Status.LOADING)
        getApiService()
            .getHotCities()
            .enqueue(object : ApiCallback<MutableList<City>>(){
                override fun onResponse(call: Call<MutableList<City>>?, result: MutableList<City>?) {
                    result?.run {
                        updateStatus(StatusEvent.Status.SUCCESS)
                        citiesLiveData.postValue(result)
                    } ?: run {
                        updateStatus(StatusEvent.Status.FAILURE)
                    }

                }

                override fun onError(call: Call<MutableList<City>>?, t: Throwable?) {
                    updateStatus(StatusEvent.Status.ERROR)
                    sendMessage(t?.message)
                }


            })
    }

    /**
     * 获取ApiService
     */
    private fun getApiService(): ApiService {
        return getRetrofitService(ApiService::class.java)
    }

    /**
     * 刷新
     */
    fun onRefresh(){
        getHotCities()
    }

}