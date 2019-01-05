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

    //get all lists from cache
    var dataMap: HashMap<LocalDateTime, List<WeatherInfoBody>> = Paper.book().read("dataList", HashMap())
    var dataMapTemp: HashMap<LocalDateTime, List<WeatherInfoBody>> = Paper.book().read("dataList", HashMap())

    //initialize function to save list in cache
    fun saveData(weatherList: List<WeatherInfoBody>){
        //get current time
        val current = LocalDateTime.now()

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formatted = current.format(formatter)
        println(formatted)

        //add list to hash map
        dataMap[current] = weatherList

        for (element in dataMap) {
            if (element.key.plusMinutes(30) >= current) {
                dataMapTemp[element.key] = element.value
            }
        }

        dataMap = dataMapTemp

        //save hash map in cache
        Paper.book().write("dataList", dataMap)

    }

    //initialize function for getting latest list from cache
    fun loadFromCache(): List<WeatherInfoBody>?{
        //check if is in cache
        return if(dataMap.size > 0) {
            //initialize temporary list
            val tempList: MutableList<LocalDateTime> = ArrayList()

            //populate temporary list
            for (element in dataMap) {
                tempList.add(element.key)
            }

            //get latest key
            val latest = Collections.max(tempList)

            //return latest list
            dataMap[latest]!!
        }else{
            null
        }
    }
}