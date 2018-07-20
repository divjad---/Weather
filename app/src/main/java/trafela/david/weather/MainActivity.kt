package trafela.david.weather

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Toast


import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import okhttp3.OkHttpClient
import retrofit2.*
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import android.support.v7.widget.DividerItemDecoration


class MainActivity : AppCompatActivity() {

    private var adapter: RecyclerView.Adapter<*>? = null
    private var layoutManager: RecyclerView.LayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        layoutManager = LinearLayoutManager(this)
        recycler_view.layoutManager = layoutManager

        val dividerItemDecoration = DividerItemDecoration(this,
                (layoutManager as LinearLayoutManager).orientation)
        recycler_view.addItemDecoration(dividerItemDecoration)

        recycler_view.adapter = adapter

        val retrofit = Retrofit.Builder()
                .baseUrl("http://meteo.arso.gov.si/")
                .client(OkHttpClient())
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build()

        val weatherService = retrofit.create(WeatherService::class.java)

        var call = weatherService.weatherInfo
        call.enqueue(object : Callback<WeatherInfo> {
            override fun onResponse(call: Call<WeatherInfo>, response: Response<WeatherInfo>) {
                Log.i("list", response.body()?.weatherDetail?.get(1)?.weatherIcon + "")

                adapter = CustomAdapter(response.body()?.weatherDetail!!, applicationContext)
                recycler_view.adapter = adapter
            }

            override fun onFailure(call: Call<WeatherInfo>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show()
                println(t.localizedMessage)
            }
        })

        swiperefresh.setOnRefreshListener {
            call = weatherService.weatherInfo
            call.enqueue(object : Callback<WeatherInfo> {
                override fun onResponse(call: Call<WeatherInfo>, response: Response<WeatherInfo>) {
                    Log.i("list", response.body()?.weatherDetail?.get(1)?.weatherIcon + "")

                    adapter = CustomAdapter(response.body()?.weatherDetail!!, applicationContext)
                    recycler_view.adapter = adapter
                }

                override fun onFailure(call: Call<WeatherInfo>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show()
                    println(t.localizedMessage)
                }
            })
            swiperefresh.isRefreshing = false
        }
    }
}
