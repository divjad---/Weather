package trafela.david.weather

import android.util.Log

import com.evernote.android.job.Job
import com.evernote.android.job.JobManager
import com.evernote.android.job.JobRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit

/**
 * Created by David Trafela on 20-Jul-18.
 */
class WeatherSyncJob : Job() {

    private val weatherService by lazy {
        WeatherService.create()
    }

    override fun onRunJob(params: Job.Params): Job.Result {
        Log.i("test", "JOB RUNNED")

        val call = weatherService.weatherInfo
        call.enqueue(object : Callback<WeatherInfo> {
            override fun onResponse(call: Call<WeatherInfo>, response: Response<WeatherInfo>) {
                val responseList: List<WeatherInfoBody> = response.body()!!.weatherDetail!!

                ManageCache().saveData(responseList)
            }

            override fun onFailure(call: Call<WeatherInfo>, t: Throwable) {
                println(t.localizedMessage)
            }
        })

        return Job.Result.SUCCESS
    }

    companion object {
        const val TAG = "job_weather_sync"

        fun scheduleJob() {
            val jobRequests = JobManager.instance().getAllJobRequestsForTag(WeatherSyncJob.TAG)
            if (!jobRequests.isEmpty()) {
                return
            }
            JobRequest.Builder(WeatherSyncJob.TAG)
                    .setPeriodic(TimeUnit.MINUTES.toMillis(15), TimeUnit.MINUTES.toMillis(10))
                    .setUpdateCurrent(true) // calls cancelAllForTag(NoteSyncJob.TAG) for you
                    .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                    .setRequirementsEnforced(true)
                    .build()
                    .schedule()
        }
    }
}
