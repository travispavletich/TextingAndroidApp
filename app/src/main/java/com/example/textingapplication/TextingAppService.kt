package com.example.textingapplication

import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.IBinder
import android.provider.Telephony
import android.sax.TextElementListener
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class TextingAppService : Service() {
    companion object {
        var TOKEN: String = ""

        var SERVER_IP = ""
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("Test", "onStartCommand")
        Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show()

        ServerMessaging.sendFirebaseToken(this)

        val scheduleTaskExecutor = Executors.newScheduledThreadPool(5)

//        getMessages()
//        val conversations = MessageHandler.getConversations(this)

//        ServerMessaging.sendConversationList(this)

//        getMessagesInConversation(94)
//        val messages = MessageHandler.getMessagesFromConversation(40, this)
//        Log.d("Length", "${messages.size}")
//        for (message in messages) {
//            Log.d("ID:34", "$message")
//        }

        return super.onStartCommand(intent, flags, startId)
    }

    fun sendMessage(recipients: Array<String>, msg: String) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS)
            != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS)
            != PackageManager.PERMISSION_GRANTED) {
            Log.d("Error", "Missing permission to read or send SMS")
        } else {
            MessageHandler.sendMessage(this, recipients, msg)
        }
    }

    private fun getPartOfMMS(mmsID: Int): String? {
        val selectionPart = "mid=$mmsID"
        val uri = Uri.parse("content://mms/part")
        val cursor = applicationContext.contentResolver.query(uri, null, selectionPart, null, null)

        cursor?.use {
            if (it.moveToFirst()) {
                do {
                    val path = it.getString(it.getColumnIndex(Telephony.Mms.Part._DATA))
                    if (path != null) {
                        return path
                    }
                } while (it.moveToNext())
            }
        }

        return null
    }

    override fun onCreate() {
        Log.d("Create", "onCreate")
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}