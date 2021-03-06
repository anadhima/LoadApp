package com.udacity


import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.provider.Settings.Global.getString
import androidx.core.app.NotificationCompat

// Notification ID.
private val NOTIFICATION_ID = 0
private val REQUEST_CODE = 0
private val FLAGS = 0


private lateinit var action: NotificationCompat.Action

fun NotificationManager.sendNotification(
    applicationContext: Context,
    messageBody: String,
    channelId: String,
    status: String) {

    // Create the content intent for the notification, which launches DetailActivity
    val contentIntent = Intent(applicationContext, DetailActivity::class.java)
    contentIntent.putExtra("fileName", messageBody)
    contentIntent.putExtra("fileStatus", status)



    val pendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    action = NotificationCompat.Action(
        null,
        applicationContext.getString(R.string.check_status),
        pendingIntent
    )

    // Build the notification
    val builder = NotificationCompat.Builder(
        applicationContext,

        // Verify the notification channel name
       channelId )


        //Set the notification icon , title, text
        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setContentTitle(applicationContext.getString(R.string.downloaded))
        .setContentText(messageBody)
        .addAction(action)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)

        .setPriority(NotificationCompat.PRIORITY_HIGH)

    notify(NOTIFICATION_ID, builder.build())
}




fun NotificationManager.cancelNotifications() {
    cancelAll()
}

