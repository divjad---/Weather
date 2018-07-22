package trafela.david.weather

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import retrofit2.*
import android.support.design.widget.Snackbar
import android.view.View
import java.text.SimpleDateFormat


class MainActivity : AppCompatActivity() {

    private var adapter: RecyclerView.Adapter<*>? = null
    private var layoutManager: RecyclerView.LayoutManager? = null

    private val weatherService by lazy {
        WeatherService.create()
    }

    var h = Handler()
    var delay = 30 * 1000 //1 second=1000 milisecond, 30*1000=30seconds
    var runnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        WeatherSyncJob.scheduleJob()

        layoutManager = LinearLayoutManager(this)
        recycler_view.layoutManager = layoutManager

        recycler_view.adapter = adapter

        var call = weatherService.weatherInfo

        if(NetworkChecks().isOnline()) {
            call.enqueue(object : Callback<WeatherInfo> {
                override fun onResponse(call: Call<WeatherInfo>, response: Response<WeatherInfo>) {
                    val responseList: List<WeatherInfoBody> = response.body()!!.weatherDetail!!

                    ManageCache().saveData(responseList)

                    adapter = CustomAdapter(responseList, applicationContext)
                    recycler_view.adapter = adapter

                    recycler_view.visibility = View.VISIBLE
                    empty_view.visibility = View.GONE
                }

                override fun onFailure(call: Call<WeatherInfo>, t: Throwable) {
                    val cacheList: List<WeatherInfoBody>? = ManageCache().loadFromCache()

                    if(cacheList != null) {
                        adapter = CustomAdapter(cacheList, applicationContext)
                        recycler_view.adapter = adapter

                        recycler_view.visibility = View.VISIBLE
                        empty_view.visibility = View.GONE
                    }else{
                        recycler_view.visibility = View.GONE
                        empty_view.visibility = View.VISIBLE
                    }

                    val parentLayout = findViewById<View>(android.R.id.content)
                    val snackbar = Snackbar
                            .make(parentLayout, "Something went wrong...", Snackbar.LENGTH_LONG)
                    snackbar.show()

                    println(t.localizedMessage)
                }
            })
        }else{
            val cacheList: List<WeatherInfoBody>? = ManageCache().loadFromCache()

            if(cacheList != null) {
                adapter = CustomAdapter(cacheList, applicationContext)
                recycler_view.adapter = adapter

                recycler_view.visibility = View.VISIBLE
                empty_view.visibility = View.GONE
            }else{
                recycler_view.visibility = View.GONE
                empty_view.visibility = View.VISIBLE
            }

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

                        recycler_view.visibility = View.VISIBLE
                        empty_view.visibility = View.GONE
                    }

                    override fun onFailure(call: Call<WeatherInfo>, t: Throwable) {
                        val cacheList: List<WeatherInfoBody>? = ManageCache().loadFromCache()

                        if(cacheList != null) {
                            adapter = CustomAdapter(cacheList, applicationContext)
                            recycler_view.adapter = adapter

                            recycler_view.visibility = View.VISIBLE
                            empty_view.visibility = View.GONE
                        }else{
                            recycler_view.visibility = View.GONE
                            empty_view.visibility = View.VISIBLE
                        }

                        val parentLayout = findViewById<View>(android.R.id.content)
                        val snackbar = Snackbar
                                .make(parentLayout, "Something went wrong...", Snackbar.LENGTH_LONG)
                        snackbar.show()

                        println(t.localizedMessage)
                    }
                })
            }else if(!NetworkChecks().isOnline() && recycler_view.adapter == null){
                val cacheList: List<WeatherInfoBody>? = ManageCache().loadFromCache()

                if(cacheList != null) {
                    adapter = CustomAdapter(cacheList, applicationContext)
                    recycler_view.adapter = adapter

                    recycler_view.visibility = View.VISIBLE
                    empty_view.visibility = View.GONE
                }else{
                    recycler_view.visibility = View.GONE
                    empty_view.visibility = View.VISIBLE
                }

                val parentLayout = findViewById<View>(android.R.id.content)
                val snackbar = Snackbar
                        .make(parentLayout, "No internet connection", Snackbar.LENGTH_LONG)

                snackbar.show()
            }
            swiperefresh.isRefreshing = false
        }
    }

    override fun onResume() {
        super.onResume()
        h.postDelayed(object : Runnable{
            override fun run() {
                //do something

                val call = weatherService.weatherInfo
                call.enqueue(object : Callback<WeatherInfo> {
                    @SuppressLint("SimpleDateFormat")
                    override fun onResponse(call: Call<WeatherInfo>, response: Response<WeatherInfo>) {
                        val responseList: List<WeatherInfoBody> = response.body()!!.weatherDetail!!

                        val cachedList: List<WeatherInfoBody>? = ManageCache().loadFromCache()

                        ManageCache().saveData(responseList)

                        if(cachedList != null){
                            val sdf = SimpleDateFormat("HH:mm")
                            val cachedTime = sdf.parse(cachedList[0].lastUpdated!!.split(" ")[1])
                            val responseTime = sdf.parse(responseList[0].lastUpdated!!.split(" ")[1])
                            if(responseTime > cachedTime){
                                adapter = CustomAdapter(responseList, applicationContext)
                                recycler_view.adapter = adapter

                                recycler_view.visibility = View.VISIBLE
                                empty_view.visibility = View.GONE
                            }
                        }
                    }

                    override fun onFailure(call: Call<WeatherInfo>, t: Throwable) {
                        println(t.localizedMessage)
                    }
                })

                runnable = this
                h.postDelayed(this, delay.toLong())
            }
        }, delay.toLong())
    }

    override fun onPause() {
        super.onPause()
        h.removeCallbacks(runnable)
    }
}
