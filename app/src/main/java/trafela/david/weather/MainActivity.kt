package trafela.david.weather

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import retrofit2.*
import android.support.v7.widget.DividerItemDecoration
import io.paperdb.Paper
import okhttp3.ResponseBody
import java.time.LocalDateTime
import android.support.design.widget.Snackbar
import android.support.v4.view.accessibility.AccessibilityEventCompat.setAction
import android.view.View
import trafela.david.weather.R.layout.activity_main
import java.util.*


class MainActivity : AppCompatActivity() {

    private var adapter: RecyclerView.Adapter<*>? = null
    private var layoutManager: RecyclerView.LayoutManager? = null

    private val weatherService by lazy {
        WeatherService.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        WeatherSyncJob.scheduleJob()

        layoutManager = LinearLayoutManager(this)
        recycler_view.layoutManager = layoutManager

        val dividerItemDecoration = DividerItemDecoration(this,
                (layoutManager as LinearLayoutManager).orientation)
        recycler_view.addItemDecoration(dividerItemDecoration)

        recycler_view.adapter = adapter

        var call = weatherService.weatherInfo

        if(NetworkChecks().isOnline()) {
            call.enqueue(object : Callback<WeatherInfo> {
                override fun onResponse(call: Call<WeatherInfo>, response: Response<WeatherInfo>) {
                    Log.i("list", response.body()?.weatherDetail?.get(1)?.weatherIcon + "")

                    ManageCache().saveData(response.body()!!.weatherDetail!!)

                    adapter = CustomAdapter(response.body()?.weatherDetail!!, applicationContext)
                    recycler_view.adapter = adapter
                }

                override fun onFailure(call: Call<WeatherInfo>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show()
                    println(t.localizedMessage)
                }
            })
        }else{
            val dataMap: HashMap<LocalDateTime, List<WeatherInfoBody>> = Paper.book().read("dataList")
            val tempList: MutableList<LocalDateTime> = ArrayList()

            for (element in dataMap) {
                tempList.add(element.key)
            }

            val minDate = Collections.max(tempList)
            println(minDate.toString() + "date")

            adapter = CustomAdapter(dataMap[minDate]!!, applicationContext)
            recycler_view.adapter = adapter

            val parentLayout = findViewById<View>(android.R.id.content)
            val snackbar = Snackbar
                    .make(parentLayout, "No internet connection", Snackbar.LENGTH_LONG)
            snackbar.show()
        }

        swiperefresh.setOnRefreshListener {
            if(NetworkChecks().isOnline()) {
                call = weatherService.weatherInfo
                call.enqueue(object : Callback<WeatherInfo> {
                    override fun onResponse(call: Call<WeatherInfo>, response: Response<WeatherInfo>) {
                        val responseList: List<WeatherInfoBody> = response.body()!!.weatherDetail!!

                        ManageCache().saveData(responseList)

                        adapter = CustomAdapter(responseList, applicationContext)
                        recycler_view.adapter = adapter
                    }

                    override fun onFailure(call: Call<WeatherInfo>, t: Throwable) {
                        Toast.makeText(this@MainActivity, "Something went wrong...Please try again later!", Toast.LENGTH_SHORT).show()
                        println(t.localizedMessage)
                    }
                })
            }else{
                val dataMap: HashMap<LocalDateTime, List<WeatherInfoBody>> = Paper.book().read("dataList")
                val tempList: MutableList<LocalDateTime> = ArrayList()

                for (element in dataMap) {
                    tempList.add(element.key)
                }

                val minDate = Collections.max(tempList)
                println(minDate.toString() + "date")

                adapter = CustomAdapter(dataMap[minDate]!!, applicationContext)
                recycler_view.adapter = adapter
                adapter = CustomAdapter(dataMap[minDate]!!, applicationContext)
                recycler_view.adapter = adapter
            }
            swiperefresh.isRefreshing = false
        }
    }
}
