package trafela.david.weather

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast


import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import okhttp3.OkHttpClient
import retrofit2.*
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

class MainActivity : AppCompatActivity() {

    //private var kk: List<WeatherInfoBody>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val retrofit = Retrofit.Builder()
                .baseUrl("http://meteo.arso.gov.si/")
                .client(OkHttpClient())
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build()

        val weatherService = retrofit.create(WeatherService::class.java)

        text.setOnClickListener{
            val call = weatherService.weatherInfo
            call.enqueue(object : Callback<WeatherInfo> {
                override fun onResponse(call: Call<WeatherInfo>, response: Response<WeatherInfo>) {
                    Log.i("list", response.body()?.weatherDetail?.get(1)?.longName + "")
                }

                override fun onFailure(call: Call<WeatherInfo>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show()
                    println(t.localizedMessage)
                }
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
