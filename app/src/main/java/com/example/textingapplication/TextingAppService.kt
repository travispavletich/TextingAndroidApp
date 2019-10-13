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

        val scheduleTaskExecutor = Executors.newScheduledThreadPool(5)

//        getMessages()
        val conversations = MessageHandler.getConversations(this)

//        for (conversation in conversations) {
////            Log.d("Conversation", "${conversation.id}: ${conversation.mostRecent}")
//            Log.d("Conversation", "$conversation")
//        }

//        sendMessage(arrayOf("4124435627", "6108830941"), "Test message, please ignore")


//        var count = 0
//        for (id in conversationIds) {
//            getMessagesInConversation(id)
//
//            if (count++ >= 300) {
//                break
//            }
//        }

        ServerMessaging.sendConversationList(this)

//        getMessagesInConversation(94)
        val messages = MessageHandler.getMessagesFromConversation(40, this)
        Log.d("Length", "${messages.size}")
        for (message in messages) {
            Log.d("ID:34", "$message")
        }

//        scheduleTaskExecutor.scheduleAtFixedRate(Runnable {
//            ServerMessaging.sendFirebaseToken(this)
//        }, 0, 5, TimeUnit.SECONDS)

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

//    fun getMessages() {
////        val messageType = Telephony.TextBasedSmsColumns.MESSAGE_TYPE_ALL;
//
////        var cursor: Cursor = applicationContext.contentResolver.query(Telephony.Mms.Sent.CONTENT_URI, null, null, null, null)!!
//
////        if (messageType == Telephony.TextBasedSmsColumns.MESSAGE_TYPE_ALL) {
//            var cursor = applicationContext.contentResolver.query(Telephony.Mms.Inbox.CONTENT_URI, null, null, null, null)!!
////        }
//
//        try {
//            if (cursor.moveToFirst()) {
//                do {
//                    val id = cursor.getInt(cursor.getColumnIndex(Telephony.Mms._ID))
//
//                    val mmsId = cursor.getString(cursor.getColumnIndex(Telephony.Mms.MESSAGE_ID))
//                    val threadId = cursor.getInt(cursor.getColumnIndex(Telephony.Mms.THREAD_ID))
//                    val date = cursor.getLong((cursor.getColumnIndex(Telephony.Mms.DATE)))
//
//                    val part = getPartOfMMS(id)
//                    val message = getMmsText(id)
//                    val sender = getMmsSender(id)
//
//                    Log.d("Message", "$sender: $message")
//                } while (cursor.moveToNext())
//            }
//        } finally {
//            cursor.close()
//        }
//    }

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

//    private fun getConversations(): String? {
//        val cursor = applicationContext.contentResolver.query(Telephony.MmsSms.CONTENT_CONVERSATIONS_URI, null, null, null, null)
//
//        cursor?.use {
//            if (it.moveToNext()) {
//                val convo = it.getString(Telephony.MmsSms.)
//            }
//        }
//    }


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