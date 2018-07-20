package trafela.david.weather

import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import retrofit2.http.GET

/**
 * Created by David Trafela on 19-Jul-18.
 */
interface WeatherService {

    @get:GET("uploads/probase/www/observ/surface/text/sl/observation_si_latest.xml")
    val weatherInfo: Call<WeatherInfo>

    companion object {
        fun create(): WeatherService {

            val retrofit = Retrofit.Builder()
                    .baseUrl("http://meteo.arso.gov.si/")
                    .client(OkHttpClient())
                    .addConverterFactory(SimpleXmlConverterFactory.create())
                    .build()

            return retrofit.create(WeatherService::class.java)
        }
    }
}
