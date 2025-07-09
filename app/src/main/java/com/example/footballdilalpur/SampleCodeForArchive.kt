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

//While using this file please remove _DEMO from all of these 3 constants
const val REQ_CODE_DEMO = 100
const val CHANNEL_ID_DEMO = "My Channel"
const val NOTIFICATION_ID_DEMO = 100


class SampleCodeForArchive : FirebaseMessagingService() {

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

        val drawable = ResourcesCompat.getDrawable(resources, R.drawable.icon, null)
        val bitmapDrawable = drawable as? BitmapDrawable
        var largeIcon: Bitmap? = null

        if (bitmapDrawable != null) {
            largeIcon = bitmapDrawable.bitmap
        }

// PendingIntent
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            REQ_CODE,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

// BigPictureStyle (Optional style)
        val bigPictureStyle = Notification.BigPictureStyle()
            .bigPicture(
                (ResourcesCompat.getDrawable(resources, R.drawable.icon, null) as? BitmapDrawable)?.bitmap
            )
            .bigLargeIcon(largeIcon)
            .setBigContentTitle("Message Heading")
            .setSummaryText("All about the message")

// InboxStyle
        val inboxStyle = Notification.InboxStyle()
            .addLine("A")
            .addLine("B")
            .addLine("C")
            .addLine("D")
            .addLine("E")
            .addLine("F")
            .addLine("G")
            .addLine("H")
            .addLine("I")
            .addLine("J")
            .addLine("K")
            .addLine("L")
            .setBigContentTitle("Inbox Style Title")
            .setSummaryText("This message from inbox style")

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val notification: Notification

// Version check for NotificationChannel (API 26+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "My Channel 1",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)

            notification = Notification.Builder(this, CHANNEL_ID)
                .setLargeIcon(largeIcon)
                .setSmallIcon(R.drawable.icon)
                .setContentText("New Message")
                .setSubText("Message from Monir")
                .setContentIntent(pendingIntent)
                //.setStyle(bigPictureStyle)
                .setStyle(inboxStyle)
                .build()
        } else {
            notification = Notification.Builder(this)
                .setLargeIcon(largeIcon)
                .setSmallIcon(R.drawable.icon)
                .setContentText("New Message")
                .setSubText("Message from Monir")
                .setContentIntent(pendingIntent)
                //.setStyle(bigPictureStyle)
                .setStyle(inboxStyle)
                .build()
        }

        notificationManager.notify(NOTIFICATION_ID, notification)

    }

}