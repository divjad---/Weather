package trafela.david.weather

/**
 * Created by David Trafela on 20-Jul-18.
 */
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import io.paperdb.Paper
import java.time.LocalDateTime
import java.util.HashMap

internal class CustomAdapter(private val weatherList: List<WeatherInfoBody>, private val context: Context) : RecyclerView.Adapter<CustomAdapter.WeatherViewHolder>() {

    private var longName: String? = null
    private var lastUpdated: String? = null
    private var windDir: String? = null
    private var windSpeed: String? = null
    private var weatherIcon: String? = null
    private var windIcon: String? = null

    private var temperature: Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomAdapter.WeatherViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.weather_info_row, parent, false)
        return WeatherViewHolder(v)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: CustomAdapter.WeatherViewHolder, position: Int) {
        val dataMap: HashMap<LocalDateTime, List<WeatherInfoBody>> = Paper.book().read("dataList")

        var temp = false

        for(element in dataMap){
            val current = LocalDateTime.now()
            if(element.key.plusMinutes(30) >= current){
                val list: List<WeatherInfoBody> = dataMap[element.key]!!
                temp = list[position].temperature!! > 20
            }
        }

        longName = weatherList[position].longName

        lastUpdated = weatherList[position].lastUpdated
        lastUpdated = lastUpdated?.split(" ")!![1]

        windDir = weatherList[position].windDir

        windSpeed = weatherList[position].windSpeed

        weatherIcon = weatherList[position].weatherIcon

        temperature = weatherList[position].temperature

        windIcon = weatherList[position].windIcon

        if(temperature != 255){
            if(temperature!! > 20 && temp) {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.holo_red_light))
            }
            holder.temperatureText.text = temperature.toString() + "°C"
        }else if(temperature == 255){
            holder.temperatureText.text = ""
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.white))
        }

        Picasso.with(context)
                .load("http://meteo.arso.gov.si/uploads/meteo/style/img/weather/" + weatherIcon + ".png")
                .into(holder.icon)

        Picasso.with(context)
                .load("http://meteo.arso.gov.si/uploads/meteo/style/img/weather/" + windIcon + ".png")
                .into(holder.wIcon)

        holder.windInfo.text = "$windDir $windSpeed m/s"
        holder.name.text = longName

        holder.updated.text = lastUpdated
    }

    override fun getItemCount(): Int {
        return weatherList.size
    }

    internal class WeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var name: TextView = itemView.findViewById(R.id.city_name)
        var updated: TextView = itemView.findViewById(R.id.last_updated)
        var temperatureText: TextView = itemView.findViewById(R.id.temp_text)
        var windInfo: TextView = itemView.findViewById(R.id.wind_detail)

        var icon: ImageView = itemView.findViewById(R.id.weather_icon)
        var wIcon: ImageView = itemView.findViewById(R.id.wind_icon)
    }
}