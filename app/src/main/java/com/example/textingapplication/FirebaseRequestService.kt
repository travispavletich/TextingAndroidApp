package com.example.textingapplication

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject

class FirebaseRequestService : FirebaseMessagingService() {
    companion object {
        val SEND_MESSAGE_STRING = "SendMessage"
        val RETRIEVE_CONVERSATIONS_STRING = "RetrieveConversations"
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.data.isNotEmpty()) {
            Log.d("Firebase", "Message data payload: ${remoteMessage.data}")

            val notificationType = remoteMessage.data.get("NotificationType")

            if (notificationType == SEND_MESSAGE_STRING) {
                handleSendMessage(remoteMessage.data)
            } else if (notificationType == RETRIEVE_CONVERSATIONS_STRING) {
                handleRetrieveConversations()
            }
        }
    }

    override fun onNewToken(token: String) {
        Log.d("Token", "New token")

        TextingAppService.TOKEN = token
        ServerMessaging.sendFirebaseToken(this)
    }

    fun handleSendMessage(message: Map<String, String>) {
        val recipientsString = message["Recipients"]
        val recipientsList = ArrayList<String>()

        if (recipientsString != null) {
            val recipientsJSON = JSONArray(recipientsString)

            for (i in 0 until recipientsJSON.length()) {
                recipientsList.add(recipientsJSON[i].toString())
            }
        }

        var recipients = Array<String>(recipientsList.size, {""})

        for (i in 0 until recipientsList.size) {
            recipients[i] = recipientsList[i]
        }

        val messageText = message["Message"]

        if (messageText != null) {
            MessageHandler.sendMessage(this, recipients, messageText)
        }
    }

    fun handleRetrieveConversations() {
        ServerMessaging.sendConversationList(this)
    }
}