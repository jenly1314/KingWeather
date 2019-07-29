package com.king.weather.app.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.legacy.app.ActionBarDrawerToggle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.services.weather.*
import com.king.base.util.LogUtils
import com.king.frame.mvvmframe.base.BaseActivity
import com.king.weather.BR
import com.king.weather.R
import com.king.weather.app.Constants
import com.king.weather.app.adapter.BindingAdapter
import com.king.weather.bean.City
import com.king.weather.databinding.MainActivityBinding
import com.king.weather.databinding.RvCityItemBinding
import com.king.weather.databinding.RvForecastItemBinding
import kotlinx.android.synthetic.main.main_activity.*


/**
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
class MainActivity : BaseActivity<MainViewModel, MainActivityBinding>(),WeatherSearch.OnWeatherSearchListener, AMapLocationListener {


    lateinit var searchView: SearchView

    lateinit var cityAdapter: BindingAdapter<City,RvCityItemBinding>

    lateinit var adapter: BindingAdapter<LocalDayWeatherForecast,RvForecastItemBinding>

    lateinit var client: AMapLocationClient

    lateinit var toggle: ActionBarDrawerToggle

    var curCity: String? = null

    var queryCity: String? = null

    override fun getLayoutId(): Int {
        return R.layout.main_activity
    }


    override fun initData(savedInstanceState: Bundle?) {
        mBinding.setVariable(BR.viewModel,mViewModel)

        initActionBar()

        srlCity.setColorSchemeResources(R.color.refresh_color)

        srl.setColorSchemeResources(R.color.refresh_color)
        srl.setOnRefreshListener {
            weatherSearch(queryCity)
        }

        rvCity.layoutManager = LinearLayoutManager(context)
        rvCity.addItemDecoration(DividerItemDecoration(context,DividerItemDecoration.VERTICAL))

        cityAdapter = BindingAdapter(context,R.layout.rv_city_item)
        cityAdapter.setOnItemClickListener { v, position ->
            var city = cityAdapter.getItem(position)

            var name = city?.takeIf { it.id > 0 }?.name ?: curCity
            weatherSearch(name)
            drawerLayout.closeDrawer(GravityCompat.START)
            searchView.setQuery("",false)

        }
        rvCity.adapter = cityAdapter

        rv.layoutManager = LinearLayoutManager(context)
        adapter = BindingAdapter(context, R.layout.rv_forecast_item)
        rv.adapter = adapter
        rv.isNestedScrollingEnabled = false

        mViewModel.citiesLiveData.observe(this,
            Observer<MutableList<City>> {
                it?.let {
                    it.add(0,City(0,getString(R.string.current_location)))
                    cityAdapter.refreshData(it)
                }

                srlCity.isRefreshing = false
            })

        initLocation()
    }

    private fun initActionBar(){

        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.menu_home)
            toggle = ActionBarDrawerToggle(this,drawerLayout,true,R.drawable.menu_home,R.string.drawer_open,R.string.drawer_close)
            toggle.syncState()
            drawerLayout.addDrawerListener(toggle)
        }
    }

    /**
     * 初始化定位
     */
    private fun initLocation(){
        client = AMapLocationClient(context)
        var option = AMapLocationClientOption()
        with(option){
            locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
            isOnceLocation = true
        }
        client.setLocationOption(option)
        client.setLocationListener(this)
        startLocation()
    }

    /**
     * 开始定位
     */
    private fun startLocation(){
        if(client.isStarted){
            return
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            //申请ACCESS_COARSE_LOCATION权限
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                Constants.LOCATION_REQUEST_CODE)
        }else{
            client.startLocation()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == Constants.LOCATION_REQUEST_CODE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                client.startLocation()
            }
        }
    }

    /**
     * 天气查询
     */
    private fun weatherSearch(city: String?){
        queryCity = city
        city?.run {
            weatherLiveSearch(city)
            weatherForecastSearch(city)
        } ?: run {
            startLocation()
            srl.isRefreshing = false
        }

    }

    /**
     * 实况天气
     */
    private fun weatherLiveSearch(city: String){
        with(WeatherSearch(context)){
            setOnWeatherSearchListener(this@MainActivity)
            query = WeatherSearchQuery(city,WeatherSearchQuery.WEATHER_TYPE_LIVE)
            searchWeatherAsyn()
        }
    }

    /**
     * 预报天气
     */
    private fun weatherForecastSearch(city: String){
        with(WeatherSearch(context)){
            setOnWeatherSearchListener(this@MainActivity)
            query = WeatherSearchQuery(city,WeatherSearchQuery.WEATHER_TYPE_FORECAST)
            searchWeatherAsyn()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_view_menu, menu)
        val searchItem = menu?.findItem(R.id.searchItem)
        searchView = searchItem?.actionView as SearchView
        with(searchView){
            queryHint = getString(R.string.hint_search)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let {
                        weatherSearch(query)
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }

            })
        }
        return super.onCreateOptionsMenu(menu)
    }

    /**
     * 实况天气回调
     */
    override fun onWeatherLiveSearched(result: LocalWeatherLiveResult?, code: Int) {
        if(code == 1000){
            result?.liveResult?.let {
                mBinding.live = it
            }
        }
        srl.isRefreshing = false
    }

    /**
     * 预报天气回调
     */
    override fun onWeatherForecastSearched(result: LocalWeatherForecastResult?, code: Int) {
        if(code == 1000){
            result?.forecastResult?.let {
                mBinding.forecast = it
                adapter.refreshData(it.weatherForecast)
            }
        }
        srl.isRefreshing = false
    }


    /**
     * 位置改变
     */
    override fun onLocationChanged(location: AMapLocation?) {
        LogUtils.d(location?.city)
        location?.city?.let {
            curCity = it
            weatherSearch(curCity)
        }
    }
}
