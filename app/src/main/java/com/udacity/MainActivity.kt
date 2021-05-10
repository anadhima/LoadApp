package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0
    private var checked = ""
    private val NOTIFICATION_ID = 0
    private lateinit var downloadManager: DownloadManager
    private lateinit var notificationManager: NotificationManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        createChannel(CHANNEL_ID, getString(R.string.notification_description))

        loading_button.setOnClickListener {
            when (radio_group.checkedRadioButtonId) {
                R.id.radio_button_glide -> {
                    checked = getString(R.string.glide_image_loading)
                    download(GLIDE_URL)

                    loading_button.setState(ButtonState.Loading)
                }
                R.id.radio_button_udacity -> {
                    checked = getString(R.string.load_app)
                    download(LOAD_APP_URL)
                    loading_button.setState(ButtonState.Loading)
                }
                R.id.radio_button_retrofit -> {
                    checked = getString(R.string.retrofit)
                    download(RETROFIT_URL)
                    loading_button.setState(ButtonState.Loading)
                }
                else -> {
                    Toast.makeText(this, "Select File", Toast.LENGTH_SHORT).show()
                }
            }

        }


    }


    //broadcastReceiver
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)


            val notificationManager = getSystemService(NotificationManager::class.java)
            var status: String? = null
            var downloadStatus: Int? = null

            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            val query = id?.let { DownloadManager.Query().setFilterById(it) }
            val cursor = downloadManager.query(query)

            if (cursor.moveToFirst()) {
                downloadStatus = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
            }

            when (downloadStatus) {
                DownloadManager.STATUS_SUCCESSFUL -> {
                    status = "Success"
                }
                DownloadManager.STATUS_FAILED -> {
                    status = "Fail"
                }
            }

            if (context != null) {
                if (status != null) {
                    notificationManager.sendNotification(
                        context,
                        checked,
                        CHANNEL_ID,
                        status
                    )
                }
            }


            loading_button.setState(ButtonState.Completed)
        }
    }

    private fun download(url: String) {
        val request =
            DownloadManager.Request(Uri.parse(url))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.

    }


    companion object {
        private const val GLIDE_URL =
            "https://github.com/bumptech/glide/archive/master.zip"
        private const val LOAD_APP_URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val RETROFIT_URL =
            "https://github.com/square/retrofit/archive/master.zip"

        private const val CHANNEL_ID = "channelId"
    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )


            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = this.getString(R.string.downloaded)
            notificationManager = getSystemService(
                NotificationManager::class.java
            ) as NotificationManager

            notificationManager.createNotificationChannel(notificationChannel)

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }


}