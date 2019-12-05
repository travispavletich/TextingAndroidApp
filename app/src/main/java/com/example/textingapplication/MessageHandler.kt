package com.example.textingapplication

import android.content.ContentUris
import android.content.Context
import com.klinker.android.send_message.Message
import com.klinker.android.send_message.Transaction
import android.net.Uri
import android.provider.ContactsContract
import android.provider.Telephony
import android.util.Log
import java.lang.Exception
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class MessageHandler {
    companion object {
        var settings = com.klinker.android.send_message.Settings()

        fun sendMessage(context: Context, recipients: Array<String>, messageText: String) {
            var recipientsString = ""

            for (rec in recipients) {
                recipientsString += "$recipients "
            }

            Log.d("SendMessage", "$messageText $recipientsString")

            settings.useSystemSending = true
            var transaction = Transaction(context, settings)

            var message = Message(messageText, recipients)

            transaction.sendNewMessage(message, Transaction.NO_THREAD_ID)


        }

        fun getConversations(context: Context): ArrayList<Conversation> {
            val projection = Array(1){"*"}
            val uri = Uri.parse("content://mms-sms/conversations?simple=true")
//            val uri = Telephony.MmsSms.CONTENT_CONVERSATIONS_URI
//            val uri = Telephony.MmsSms.CONTENT_URI

            val cursor = context.contentResolver.query(uri, projection, null, null, null)

            var conversations = ArrayList<Conversation>()

            var col = true

            cursor?.use {
                if (it.moveToFirst()) {
                    do {
                        if (col) {
                            for (i in 0..200) {
                                try {
                                    val columnName = it.getColumnName(i)
                                    if (columnName != null) {
                                        Log.d("Column", columnName)
                                    } else {
                                        Log.d("Column", "Empty")
                                    }
                                } catch (e: Exception) {

                                }
                            }
                            col = false
                        }
//                        val id = it.getInt(it.getColumnIndex(Telephony.MmsSms._ID))
                        val timestamp = it.getLong(it.getColumnIndex("date"))
//                        val convo_id = it.getInt(it.getColumnIndex("thread_id"))
                        val convo_id = it.getInt(it.getColumnIndex("_id"))
//                        val body = it.getString(it.getColumnIndex("body"))
                        val body = it.getString(it.getColumnIndex("snippet"))
//                        val ct_t = it.getString(it.getColumnIndex("ct_t"))
                        var recipientId = it.getString(it.getColumnIndex("recipient_ids"))
//                        var person = "4402123045"

                        if (recipientId.contains(" ")) {
                            continue
                        }

                        var person = getPhoneNumbertByRecipientId(it.getLong(it.getColumnIndex("recipient_ids")), context)

                        if (person != null) {
                            person = person.replace("+", "")
                        }

                        val contactUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                                    Uri.encode(person))

                        val contactProjection = arrayOf(ContactsContract.PhoneLookup.DISPLAY_NAME)

                        var contactName = ""

                        val contactCursor = context.contentResolver.query(contactUri, contactProjection, null, null, null)

                        contactCursor?.use { contacts ->
                            if (contacts.moveToFirst()) {
                                contactName = contacts.getString(0)
                            }
                        }

                        val date = timestampToDate(timestamp)

//                        Log.d("Date", date)
//
//                        Log.d("Person", "$convo_id $person")


                        if (person != null) {
                            if (contactName == "") {
                                contactName = person
                            }

                            val conversation = Conversation(convo_id, body ?: "", arrayOf(person), arrayOf(contactName), timestamp, date)
                            Log.d("Conversation", "$conversation")
                            conversations.add(conversation)
                        } else {
                            Log.d("Nulls", "$convo_id")
                        }

                    } while (it.moveToNext())
                }
            }

            return conversations
        }

        fun getMessagesFromConversation(conversationId: Int, context: Context): ArrayList<com.example.textingapplication.Message> {
            val projection = arrayOf("_id", "ct_t")
            val uri = Uri.parse("content://mms-sms/conversations/$conversationId")

            val cursor = context.contentResolver.query(uri, projection, null, null, null)

            val messageList = ArrayList<com.example.textingapplication.Message>()

            cursor?.use {
                if (it.moveToFirst()) {
                    do {
                        val messageId = it.getInt(it.getColumnIndex(Telephony.MmsSms._ID))
                        val messageType = it.getString(it.getColumnIndex("ct_t"))

                        if ("application/vnd.wap.multipart.related" == messageType) {
                            // Mms stuff
                            val message = getMMSMessageData(messageId, context)
                            if (message != null) {
                                messageList.add(message)
                            }
                        } else {
                            // Sms stuff
                            val message = getSMSMessageData(messageId, context)

                            if (message != null) {
                                messageList.add(message)
                            }
                        }
                    } while (it.moveToNext())
                }
            }

            return messageList
        }

        private fun getSMSMessageData(id: Int, context: Context): com.example.textingapplication.Message? {
            val selection = "_id=$id"

            val uri = Uri.parse("content://sms")
            val cursor = context.contentResolver.query(uri, null, selection, null, null)

            cursor?.use {
                if (it.moveToFirst()) {
                    val readColumn = it.getColumnIndex("read")

                    val phone = it.getString(it.getColumnIndex("address"))
                    val type = it.getInt(it.getColumnIndex("type"))
                    val timestamp = it.getLong(it.getColumnIndex("date"))
                    val body = it.getString(it.getColumnIndex("body"))
                    val read = it.getInt(it.getColumnIndex("read"))

                    val date = timestampToDate(timestamp)

                    val messageData = "$date $phone $body $read"

                    return Message(phone, type == 2, body, id, date, timestamp)
                }
            }

            return null
        }

        private fun getMMSMessageData(id: Int, context: Context): com.example.textingapplication.Message? {
            val selection = "_id=$id"

            val uri = Uri.parse("content://mms/")
            val cursor = context.contentResolver.query(uri, null, selection, null, null)

            // Get whether or not the message has been read
            var read = 0
            var timestamp: Long = 0
            cursor?.use {
                if (cursor.moveToFirst()) {
                    read = it.getInt(it.getColumnIndex("read"))
                    timestamp = it.getLong(it.getColumnIndex("date"))
                }
            }

            val body = getMmsText(id, context)
            val phone = getMmsSender(id, context)

            val date = timestampToDate(timestamp)

            if (body != null && phone != null) {
                return Message(phone, false, body, id, date, timestamp)
            }

            return null
        }

        private fun getMmsText(id: Int, context: Context): String? {
            val selectionPart = "mid=$id"

            val uri = Uri.parse("content://mms/part")
            val cursor = context.applicationContext.contentResolver.query(uri, null, selectionPart, null, null)

            cursor?.use {
                if (it.moveToFirst()) {
                    do {
                        val type = it.getString(it.getColumnIndex(Telephony.Mms.Part.CONTENT_TYPE))

                        if ("text/plain" == type) {
                            val path = it.getString(it.getColumnIndex(Telephony.Mms.Part.TEXT))
                            if (path != null) {
                                return path
                            }
                        }
                    } while (it.moveToNext())
                }
            }

            return null
        }

        private fun getMmsSender(id: Int, context: Context): String? {
            val selectionPart = "msg_id=$id"

            val uri = Uri.parse("content://mms/$id/addr")
            var cursor = context.applicationContext.contentResolver.query(uri, null, selectionPart, null, null)

            cursor?.use {
                if (it.moveToFirst()) {
                    do {
                        val sender = it.getString(it.getColumnIndex("address"))
                        val senderId = it.getInt(it.getColumnIndex(Telephony.Mms.Addr.CONTACT_ID));

                        if (sender != null) {
                            return sender
                        }
                    } while (it.moveToNext())
                }
            }

            return null
        }

        private fun getPhoneNumbertByRecipientId(recipientId: Long, context: Context): String? {
            val uri = ContentUris.withAppendedId(Uri.parse("content://mms-sms/canonical-address"), recipientId)
            val cursor = context.contentResolver.query(uri, null, null, null, null)

            var phoneNumber: String? = null

            cursor?.use {
                if (it.moveToFirst()) {
                    phoneNumber = cursor.getString(0)
                }
            }

            return phoneNumber
        }

        fun timestampToDate(timestamp: Long): String {
//            val formatter = DateTimeFormatter.ofPattern("", Locale.ENGLISH)
//            val localDate = LocalDateTime.parse(date, formatter)
//
//            val timestamp = localDate.atOffset(ZoneOffset.UTC).toInstant().toEpochMilli()

            val formatter = SimpleDateFormat("MM/dd/yyyy hh:mm:ss", Locale.ENGLISH)
            return formatter.format(Date(timestamp))
        }
    }
}