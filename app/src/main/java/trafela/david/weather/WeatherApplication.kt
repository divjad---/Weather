package trafela.david.weather

import android.app.Application
import android.util.Log

import com.evernote.android.job.JobManager

/**
 * Created by David Trafela on 20-Jul-18.
 */
class WeatherApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        JobManager.create(this).addJobCreator(WeatherJobCreator())
    }
}
