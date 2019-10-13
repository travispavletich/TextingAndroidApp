package com.example.textingapplication

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseRequestService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.data.isNotEmpty()) {
            Log.d("Firebase", "Message data payload: ${remoteMessage.data}")
        }
    }

    override fun onNewToken(token: String) {
        Log.d("Token", "New token")

        TextingAppService.TOKEN = token
        ServerMessaging.sendFirebaseToken(this)
    }
}