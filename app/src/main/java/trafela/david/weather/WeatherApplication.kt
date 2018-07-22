package trafela.david.weather

import android.app.Application

import com.evernote.android.job.JobManager
import io.paperdb.Paper

/**
 * Created by David Trafela on 20-Jul-18.
 */
class WeatherApplication : Application() {

    //initialize job manager and paper
    override fun onCreate() {
        super.onCreate()
        JobManager.create(this).addJobCreator(WeatherJobCreator())
        Paper.init(this)
    }
}
