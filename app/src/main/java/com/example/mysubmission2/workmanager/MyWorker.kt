package com.example.mysubmission2.workmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.mysubmission2.R
import com.example.mysubmission2.data.remote.response.NewNotificationResponse
import com.example.mysubmission2.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyWorker(context: Context, workerparams:  WorkerParameters) : Worker(context, workerparams) {
    private var resultStatus: Result? = null

    override fun doWork(): Result {
        return getNewEventNotifications()
    }

    private fun getNewEventNotifications(): Result {
        Log.e(TAG, "MyWorker: Mulai ....")
        Looper.prepare()
        val client = ApiConfig.getApiService().getNewNotification()
        client.enqueue(object : Callback<NewNotificationResponse> {
            override fun onResponse(call: Call<NewNotificationResponse>, response: Response<NewNotificationResponse>) {
                try {
                    if (response.isSuccessful) {
                        val listNewNotification = response.body()?.listNewNotification
                        listNewNotification?.forEach {
                            val title = it.name
                            val time = it.beginTime
                            showNotifications(title, time)
                        }
                        Log.e(TAG, "onResponse: Selesai....")
                        resultStatus = Result.success()
                    }
                } catch (e: Exception) {
                    showNotifications("Data Gagal Ditampilkan onResponse", e.message as String)
                    Log.e(TAG, "onResponse: Gagal......")
                    resultStatus = Result.failure()
                }
            }

            override fun onFailure(call: Call<NewNotificationResponse>, t: Throwable) {
                Log.e(TAG, "onFailur: Gagal ...")
                showNotifications("Data Gagal Ditampilkan onFailure", t.message as String)
                resultStatus = Result.failure()
            }
        })
        return resultStatus as Result
    }

    private fun showNotifications(title: String, time: String) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications)
            .setContentTitle(title)
            .setContentText(time)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            notification.setChannelId(CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(NOTIFICATION_ID, notification.build())
    }

    companion object {
        private const val TAG = "MyWorker Test "
        const val NOTIFICATION_ID = 1
        const val CHANNEL_ID = "channel_01"
        const val CHANNEL_NAME = "dicoding channel"
    }
}