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

                    val time = msgs[0].timestampMillis

                    for (msg in msgs) {
                        body += msg.messageBody
                    }

                    val uri = Uri.parse("content://mms-sms/")
                    val cursor = context?.contentResolver?.query(uri, null , null, null, null)

                    cursor?.use {
                        android.provider.Telephony.Sms.DATE
                        val person = it.getString(it.getColumnIndex("address"))
                        val timestamp = it.getLong(it.getColumnIndex("date"))
                    }

                } catch (e: Exception) {
                    Log.d("Exception caught", e.message)
                }
            }
        }
    }
}