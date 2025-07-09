package com.example.footballdilalpur

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.util.Log
import androidx.core.content.res.ResourcesCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

const val REQ_CODE = 100
const val CHANNEL_ID = "My Channel"
const val NOTIFICATION_ID = 100


class FirebaseMsgService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("Refreshed Token : ", token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        message.notification?.let {
            notification ->
            val title = notification.title
            val msg = notification.body

            if(title != null && msg != null)
                pushNotification(title, msg)
        }

    }

    private fun pushNotification(title : String, msg : String) {

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val notification: Notification

        //Pending Intent
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            REQ_CODE,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        // Version check for NotificationChannel (API 26+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val name : CharSequence = "Custom Channel"
            val description : String = "Channel for push notifications"
            val importance : Int = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = description

            notificationManager.createNotificationChannel(channel)

            notification = Notification.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.icon)
                .setContentIntent(pendingIntent)
                .setContentTitle(title)
                .setSubText(msg)
                .setAutoCancel(true)
                .setChannelId(CHANNEL_ID)
                .build()
        } else {
            notification = Notification.Builder(this)
                .setSmallIcon(R.drawable.icon)
                .setContentIntent(pendingIntent)
                .setContentTitle(title)
                .setSubText(msg)
                .setAutoCancel(true)
                .build()
        }

        notificationManager.notify(NOTIFICATION_ID, notification)

    }

}