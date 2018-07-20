package trafela.david.weather

/**
 * Created by David Trafela on 20-Jul-18.
 */
import android.annotation.SuppressLint
import android.content.Context
import android.opengl.Visibility
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

internal class CustomAdapter(private val weatherList: List<WeatherInfoBody>, val context: Context) : RecyclerView.Adapter<CustomAdapter.WeatherViewHolder>() {

    private var longName: String? = null
    private var lastUpdated: String? = null
    private var windDir: String? = null
    private var windSpeed: String? = null
    private var weatherIcon: String? = null

    private var temperature: Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomAdapter.WeatherViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.weather_info_row, parent, false)
        return WeatherViewHolder(v)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: CustomAdapter.WeatherViewHolder, position: Int) {

        longName = weatherList[position].longName

        lastUpdated = weatherList[position].lastUpdated
        lastUpdated = lastUpdated?.split(" ")!![1]

        windDir = weatherList[position].windDir

        windSpeed = weatherList[position].windSpeed

        weatherIcon = weatherList[position].weatherIcon

        temperature = weatherList[position].temperature

        if(temperature.toString() != "null"){
            if(temperature!! > 20) {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.holo_red_light))
            }
            holder.temperatureText.text = temperature.toString() + "°C"
        }else if(temperature.toString() == "null"){
            holder.temperatureText.text = ""
        }

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
    }
}