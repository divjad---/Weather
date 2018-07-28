package trafela.david.weather

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class WeatherItemViewModel: ViewModel() {
    private var list = MutableLiveData<List<WeatherInfoBody>>()

    //initialize retrofit instance
    private val weatherService by lazy {
        WeatherService.create()
    }

    /* Called on app launch */
    fun initNetworkRequest() {
        //initialize retrofit call
        val call = weatherService.weatherInfo

        call.enqueue(object : Callback<WeatherInfo> {
            override fun onResponse(call: Call<WeatherInfo>, response: Response<WeatherInfo>) {
                //load response into list
                val responseList: List<WeatherInfoBody> = response.body()!!.weatherDetail!!

                //save list to cache
                ManageCache().saveData(responseList)

                list.value = responseList
            }

            override fun onFailure(call: Call<WeatherInfo>, t: Throwable) {
                println(t.localizedMessage)
                list.value = null
            }
        })
    }

    fun getList(): LiveData<List<WeatherInfoBody>>?{
        return list
    }
}