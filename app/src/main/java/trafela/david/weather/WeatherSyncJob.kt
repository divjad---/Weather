package trafela.david.weather

import android.util.Log

import com.evernote.android.job.Job
import com.evernote.android.job.JobManager
import com.evernote.android.job.JobRequest
import java.util.concurrent.TimeUnit

/**
 * Created by David Trafela on 20-Jul-18.
 */
class WeatherSyncJob : Job() {

    override fun onRunJob(params: Job.Params): Job.Result {
        Log.i("test", "JOB RUNNED")
        return Job.Result.SUCCESS
    }

    companion object {
        const val TAG = "job_note_sync"

        fun scheduleJob() {
            val jobRequests = JobManager.instance().getAllJobRequestsForTag(WeatherSyncJob.TAG)
            if (!jobRequests.isEmpty()) {
                return
            }
            JobRequest.Builder(WeatherSyncJob.TAG)
                    .setPeriodic(TimeUnit.MINUTES.toMillis(15), TimeUnit.MINUTES.toMillis(7))
                    .setUpdateCurrent(true) // calls cancelAllForTag(NoteSyncJob.TAG) for you
                    .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                    .setRequirementsEnforced(true)
                    .build()
                    .schedule()
        }
    }
}
