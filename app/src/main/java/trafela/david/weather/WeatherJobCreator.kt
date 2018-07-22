package trafela.david.weather

import com.evernote.android.job.Job
import com.evernote.android.job.JobCreator

/**
 * Created by David Trafela on 20-Jul-18.
 */
class WeatherJobCreator : JobCreator {

    //create job
    override fun create(tag: String): Job? {
        return when (tag) {
            WeatherSyncJob.TAG -> WeatherSyncJob()
            else -> null
        }
    }
}
