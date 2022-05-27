package com.example.countdown.worker

import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.countdown.utils.DateUtils
import java.util.concurrent.TimeUnit

class NotificationManager {
    companion object {
        /**
         * encola oneTimeNotification
         * @param desc recibe String
         * @param hour recibe String
         * @param dateEnd recibe String
         **/

        fun scheduleOneTimeNotification(desc: String, hour: String, dateEnd: String) {
            val workText = "$desc a las $hour"
            val milliseconds = DateUtils.calculateMilliseconds(dateEnd)

            val data = Data.Builder()
                .putString("WORK_DATA", workText)
                .build()

            val work =
                OneTimeWorkRequestBuilder<com.example.countdown.worker.WorkManager>()
                    .setInitialDelay(milliseconds, TimeUnit.MILLISECONDS)
                    .addTag("WORK_$desc")
                    .setInputData(data)

                    .build()


            WorkManager.getInstance().enqueue(work)
        }

        fun cancelNotification(desc:String){
            WorkManager.getInstance().cancelAllWorkByTag(desc)
        }
    }
}