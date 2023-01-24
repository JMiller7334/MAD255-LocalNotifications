package com.example.mad_255localnotifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import com.google.android.material.snackbar.Snackbar

/*
DOCUMENTATION FOR MYSELF
Result activity is the activitiy the user will land on if they
tap on the notification

gradleModule>MinSKD needs to be set - check code below.

creating the notification service should be done high up in the
class MainActivity.

a permission in the manifest(i think) needs to be set - this can be done
by right clicking the error and letting studio grant the needed permission.
This happens at the send notification func I believe.
 */

class MainActivity : AppCompatActivity() {

    private lateinit var notificationManager: NotificationManager
    private lateinit var channelID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //create the notification service
        notificationManager = getSystemService(
            NOTIFICATION_SERVICE
        ) as NotificationManager
        val areNotificationPermissionsEnabled: Boolean =
            notificationManager.areNotificationsEnabled()

        channelID = "com.example.MAD255"

        var buttonNotify: Button = findViewById<Button>(R.id.button)
        buttonNotify.setOnClickListener{
            if (areNotificationPermissionsEnabled == false){

                //SNACKBAR WITH A BUTTON TO OPEN SETTINGS FOR THE APP//
                //handles if app notifications are disabled.
                val snackbar = Snackbar.make(it,
                    "Please enable app notifications",
                    Snackbar.LENGTH_INDEFINITE)
                snackbar.setAction("Open Settings", View.OnClickListener {
                    val intent = Intent(
                        android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", packageName, null)
                    )
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                })
                snackbar.show()
            } else {
                //send notification
                sendNotification("Test", "This is a test message")
            }
        }
        //create the channel
        createNotificationChannel(channelID,
        "Local Notify Default")

    }

    fun sendNotification(title: String, content: String){
        val notificationID = 101
        val icon: Icon = Icon.createWithResource(
            this, R.drawable.ic_launcher_background
        )
        val resultIntent = Intent(
            this, ResultActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,0, resultIntent,0)
        val action: Notification.Action = Notification.Action.Builder(
            icon, "Open", pendingIntent).build()
        val notification = Notification.Builder(this@MainActivity, channelID)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(icon)
            .setChannelId(channelID)
            .setColor(Color.GREEN)
            .setContentIntent(pendingIntent)
            .setActions(action)
            .setNumber(notificationID)
            .build()

        notificationManager?.notify(notificationID, notification)

    }

    //require min ver of 26 set this in the gradle module>minSDK
    // OR right click error set @RequireAPI
    fun createNotificationChannel(id: String, name: String){
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(id, name, importance).apply {
            enableLights(true)
            lightColor = Color.RED
            enableVibration(true)
            vibrationPattern = longArrayOf(100, 200, 300)
        }
        notificationManager?.createNotificationChannel(channel)
    }
}