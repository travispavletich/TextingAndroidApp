package com.example.textingapplication

class Conversation(val ConversationID: Int, val MostRecent: String, val Participants: Array<String>, val Contacts: Array<String>, val TimeStampMillis: Long, val MostRecentTimestamp: String) {
    override fun toString(): String {
        var participantsString = ""

        for (part in Participants) {
            participantsString += "$part "
        }

        return "$ConversationID: ($participantsString) Contact: $Contacts Preview: $MostRecent"
//        return "$id"
    }
}

class ConversationList(val Conversations: ArrayList<Conversation>) {
}