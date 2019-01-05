package trafela.david.weather

/**
 * Created by David Trafela on 20-Jul-18.
 */
import android.annotation.SuppressLint
import android.content.Context
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

    private var mExpandedPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomAdapter.WeatherViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.weather_info_row, parent, false)
        return WeatherViewHolder(v)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        //get cached data
        val dataMap: HashMap<LocalDateTime, List<WeatherInfoBody>> = Paper.book().read("dataList")

        //check if temperature was above 20 for past half hour and set variable
        var temp = false

        for (element in dataMap) {
            val current = LocalDateTime.now()
            if (element.key.plusMinutes(30) >= current) {
                val list: List<WeatherInfoBody> = dataMap[element.key]!!
                if (list.size != position && weatherList[position].longName == list[position].longName) {
                    temp = list[position].temperature!! > 20
                }
            }
        }

        //set values to variables with latest info
        val longName: String = weatherList[position].longName!!

        var lastUpdated: String = weatherList[position].lastUpdated!!
        lastUpdated = lastUpdated.split(" ")[1]

        val windDir: String = weatherList[position].windDir!!

        val windSpeed: String = weatherList[position].windSpeed!!

        val weatherIcon: String = weatherList[position].weatherIcon!!

        val temperature: Int = weatherList[position].temperature!!

        val windIcon: String = weatherList[position].windIcon!!

        val humidity: String = weatherList[position].humidity!!

        val visibility: String = weatherList[position].visibility!!

        var sunrise: String = weatherList[position].sunrise!!
        sunrise = sunrise.split(" ")[1]

        var sunset: String = weatherList[position].sunset!!
        sunset = sunset.split(" ")[1]

        //change color of background if temperature was above 20
        if(temperature != 255){
            if(temperature > 20 && temp) {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.temp_above))
            }else{
                holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.details_background))
            }
            holder.temperatureText.text = temperature.toString() + "°C"
        }else if(temperature == 255){
            holder.temperatureText.text = ""
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.details_background))
        }

        //load images
        Picasso.with(context)
                .load("http://meteo.arso.gov.si/uploads/meteo/style/img/weather/$weatherIcon.png")
                .fit()
                .into(holder.icon)

        Picasso.with(context)
                .load("http://meteo.arso.gov.si/uploads/meteo/style/img/weather/$windIcon.png")
                .fit()
                .into(holder.wIcon)

        //set UI elements
        holder.windInfo.text = "$windDir $windSpeed m/s"

        holder.name.text = longName

        holder.updated.text = "Updated: $lastUpdated"

        holder.humidityText.text = "Humidity: $humidity%"

        holder.visibilityText.text = "Visibility: $visibility km"

        holder.sunsetText.text = "Sunset: $sunset"
        holder.sunriseText.text = "Sunrise: $sunrise"

        //initialize details screen and handle expansion
        val isExpanded = position == mExpandedPosition

        holder.humidityText.visibility = if (isExpanded && humidity != "") View.VISIBLE else View.GONE
        holder.visibilityText.visibility = if (isExpanded && visibility != "") View.VISIBLE else View.GONE
        holder.sunriseText.visibility = if (isExpanded && sunrise != "") View.VISIBLE else View.GONE
        holder.sunsetText.visibility = if (isExpanded && sunset != "") View.VISIBLE else View.GONE

        holder.itemView.isActivated = isExpanded
        holder.itemView.setOnClickListener {
            mExpandedPosition = if (isExpanded) -1 else position
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return weatherList.size
    }

    internal class WeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        //initialize UI elements
        var name: TextView = itemView.findViewById(R.id.city_name)
        var updated: TextView = itemView.findViewById(R.id.last_updated)
        var temperatureText: TextView = itemView.findViewById(R.id.temp_text)
        var windInfo: TextView = itemView.findViewById(R.id.wind_detail)
        var humidityText: TextView = itemView.findViewById(R.id.humidity_text)
        var visibilityText: TextView = itemView.findViewById(R.id.visibility_text)
        var sunriseText: TextView = itemView.findViewById(R.id.sunrise_text)
        var sunsetText: TextView = itemView.findViewById(R.id.sunset_text)

        var icon: ImageView = itemView.findViewById(R.id.weather_icon)
        var wIcon: ImageView = itemView.findViewById(R.id.wind_icon)
    }
}