package trafela.david.weather

import android.content.Context
import io.paperdb.Paper
import java.time.LocalDateTime
import java.util.HashMap
import java.time.format.DateTimeFormatter


/**
 * Created by David Trafela on 20-Jul-18.
 */
class ManageCache{

    var dataMap: HashMap<LocalDateTime, List<WeatherInfoBody>> = Paper.book().read("dataList")

    fun saveData(weatherList: List<WeatherInfoBody>){
        val current = LocalDateTime.now()

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formatted = current.format(formatter)
        println(formatted)

        dataMap[current] = weatherList

        Paper.book().write("dataList", dataMap)
    }
}