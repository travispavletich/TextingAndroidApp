package com.example.textingapplication

class Message (val Sender: String, val IsSender: Boolean, val MessageBody: String, val ConversationID: Int){
    override fun toString(): String {
        return when (IsSender) {
            true -> "Sent to $Sender: $MessageBody"
            false -> "Received from $Sender: $MessageBody"
        }
    }
}

class MessageList(val Messages: ArrayList<Message>) {
}
