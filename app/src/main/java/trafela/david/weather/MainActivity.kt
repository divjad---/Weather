package trafela.david.weather

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import retrofit2.*
import android.support.design.widget.Snackbar
import android.view.View

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
            swiperefresh.isRefreshing = false
        }
    }
}
