package trafela.david.weather

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import android.support.design.widget.Snackbar
import android.view.View
import io.paperdb.Paper
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
import kotlin.jvm.java

class MainActivity : AppCompatActivity() {

    //initialize variables
    private var adapter: RecyclerView.Adapter<*>? = null
    private var layoutManager: RecyclerView.LayoutManager? = null

    //initialize handler for refreshing every 30s
    var h = Handler()
    var delay = 60 * 1000 //1 second=1000 millisecond, 30*1000=30seconds
    var runnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        //schedule job every 15 minutes
        WeatherSyncJob.scheduleJob()

        recycler_view.setHasFixedSize(true)
        recycler_view.setItemViewCacheSize(20)
        recycler_view.isDrawingCacheEnabled = true
        recycler_view.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH

        val viewModel = ViewModelProviders.of(this).get(WeatherItemViewModel::class.java)
        viewModel.getList()!!.observe(this, Observer { list ->

            if(list != null){
                adapter = CustomAdapter(list, applicationContext)
                recycler_view.adapter = adapter

                recycler_view.visibility = View.VISIBLE
                empty_view.visibility = View.GONE

            }else{
                //get list from cache in case of failure
                val cacheList: List<WeatherInfoBody>? = ManageCache().loadFromCache()

                //load list into recycler view if exists else show empty view
                if(cacheList != null) {
                    adapter = CustomAdapter(cacheList, applicationContext)
                    recycler_view.adapter = adapter

                    recycler_view.visibility = View.VISIBLE
                    empty_view.visibility = View.GONE
                }else{
                    recycler_view.visibility = View.GONE
                    empty_view.visibility = View.VISIBLE
                }

                //show snackbar with message
                val parentLayout = findViewById<View>(android.R.id.content)
                val snackbar = Snackbar
                        .make(parentLayout, "Something went wrong...", Snackbar.LENGTH_LONG)
                snackbar.show()
            }

        })

        //initialize recycler view
        layoutManager = LinearLayoutManager(this)
        recycler_view.layoutManager = layoutManager

        recycler_view.adapter = adapter

        //check for internet connection
        if(NetworkChecks().isOnline()) {
            viewModel.initNetworkRequest()
        }else{
            //get list from cache in case of failure
            val cacheList: List<WeatherInfoBody>? = ManageCache().loadFromCache()

            //load list into recycler view if exists else show empty view
            if(cacheList != null) {
                adapter = CustomAdapter(cacheList, applicationContext)
                recycler_view.adapter = adapter

                recycler_view.visibility = View.VISIBLE
                empty_view.visibility = View.GONE
            }else{
                recycler_view.visibility = View.GONE
                empty_view.visibility = View.VISIBLE
            }

            //show snackbar with message
            val parentLayout = findViewById<View>(android.R.id.content)
            val snackbar = Snackbar
                    .make(parentLayout, "No internet connection", Snackbar.LENGTH_LONG)
            snackbar.show()

            //println(t.localizedMessage)
        }

        swiperefresh.setOnRefreshListener {
            //check for internet connection
            if(NetworkChecks().isOnline()) {
                viewModel.initNetworkRequest()
            }else{
                //get list from cache in case of failure
                val cacheList: List<WeatherInfoBody>? = ManageCache().loadFromCache()

                //load list into recycler view if exists else show empty view
                if(cacheList != null) {
                    adapter = CustomAdapter(cacheList, applicationContext)
                    recycler_view.adapter = adapter

                    recycler_view.visibility = View.VISIBLE
                    empty_view.visibility = View.GONE
                }else{
                    recycler_view.visibility = View.GONE
                    empty_view.visibility = View.VISIBLE
                }

                //show snackbar with message
                val parentLayout = findViewById<View>(android.R.id.content)
                val snackbar = Snackbar
                        .make(parentLayout, "No internet connection", Snackbar.LENGTH_LONG)
                snackbar.show()

                //println(t.localizedMessage)
            }

            //end refreshing
            swiperefresh.isRefreshing = false
        }
    }

    override fun onResume() {
        super.onResume()
        h.postDelayed(object : Runnable{
            override fun run() {
                //do something
                if(NetworkChecks().isOnline()) {
                    val viewModel = ViewModelProviders.of(this@MainActivity).get(WeatherItemViewModel::class.java)
                    viewModel.initNetworkRequest()
                }
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