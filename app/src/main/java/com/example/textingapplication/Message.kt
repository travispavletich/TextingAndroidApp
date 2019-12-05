package com.example.textingapplication

class Message (val Sender: String, val IsSender: Boolean, val MessageBody: String, val ConversationID: Int, val TimeStamp: String, val TimeStampMillis: Long){
    override fun toString(): String {
        return when (IsSender) {
            true -> "Sent to $Sender $ConversationID: $MessageBody"
            false -> "Received from $Sender $ConversationID: $MessageBody"
        }
    }
}

class MessageList(val Messages: ArrayList<Message>, val ConversationID: Int) {
}
