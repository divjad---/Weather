package trafela.david.weather

import io.paperdb.Paper
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.HashMap


/**
 * Created by David Trafela on 20-Jul-18.
 */
class ManageCache{

    var dataMap: HashMap<LocalDateTime, List<WeatherInfoBody>> = Paper.book().read("dataList", HashMap())

    fun saveData(weatherList: List<WeatherInfoBody>){

        val current = LocalDateTime.now()

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formatted = current.format(formatter)
        println(formatted)

        dataMap[current] = weatherList

        Paper.book().write("dataList", dataMap)

    }

    fun loadFromCache(): List<WeatherInfoBody>?{
        return if(dataMap.size > 0) {
            val tempList: MutableList<LocalDateTime> = ArrayList()

            for (element in dataMap) {
                tempList.add(element.key)
            }

            val latest = Collections.max(tempList)

            dataMap[latest]!!
        }else{
            null
        }
    }
}