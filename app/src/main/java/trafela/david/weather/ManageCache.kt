package trafela.david.weather

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


/**
 * Created by David Trafela on 20-Jul-18.
 */
class ManageCache{

    var dataMap: HashMap<LocalDateTime, List<WeatherInfoBody>> = HashMap()

    fun saveData(weatherList: List<WeatherInfoBody>){
        val current = LocalDateTime.now()

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formatted = current.format(formatter)
        println(formatted)

        dataMap[current] = weatherList
    }
}