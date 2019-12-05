package com.example.textingapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.telephony.SmsMessage
import android.util.Log
import java.lang.Exception

class SmsListener : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

        Log.d("Receive SMS", "Got a message")

        if (intent?.action == "android.provider.Telephony.SMS_RECEIVED") {
            val bundle = intent.extras
            var msgs: Array<SmsMessage>? = null

            var msg_from: String? = null

            if (bundle != null) {
                val intentExtras: Bundle? = intent.extras

                try {
                    val pdus: Array<Any> =  bundle.get("pdus") as Array<Any>
                    msgs = Array(pdus.size) {
                        val format = intentExtras?.getString("format")
                        SmsMessage.createFromPdu(pdus[it] as ByteArray, format)
                    }

                    var body = ""
                    val sender = if (msgs.isNotEmpty()) msgs[0].originatingAddress else ""

                    val timestamp = msgs[0].timestampMillis
                    val date = MessageHandler.timestampToDate(timestamp)

                    for (msg in msgs) {
                        body += msg.messageBody
                    }

                    val uri = Uri.parse("content://sms")
                    val cursor = context?.contentResolver?.query(uri, null , null, null, null)

                    var conversation_id = 0

                    cursor?.use {
                        if (it.moveToFirst()) {
                            val person = it.getString(it.getColumnIndex("address"))
                            val messageTimestamp = it.getLong(it.getColumnIndex("date"))
                            val convoId = it.getInt(it.getColumnIndex("thread_id"))

//                            if (person == sender && messageTimestamp == timestamp) {
                            if (person == sender) {
                                conversation_id = convoId
                            }
                        }
                    }

                    if (sender != null) {
                        val message = Message(sender, false, body, conversation_id, date, timestamp)
                        Log.d("NewMessage", "$message")
                        ServerMessaging.sendNewMessage(context!!, message)
                    } else {
                        Log.d("NewMessage", "Sender is null")
                    }

                } catch (e: Exception) {
                    Log.d("Exception caught", e.message)
                }
            }
        }
    }
}