package trafela.david.weather

import retrofit2.Call
import retrofit2.http.GET

/**
 * Created by David Trafela on 19-Jul-18.
 */
interface WeatherService {

    @get:GET("uploads/probase/www/observ/surface/text/sl/observation_si_latest.xml")
    val weatherInfo: Call<WeatherInfo>
}
