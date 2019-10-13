package com.example.textingapplication

class Conversation(val ConversationID: Int, val MostRecent: String, val Participants: Array<String>) {
    override fun toString(): String {
        var participantsString = ""

        for (part in Participants) {
            participantsString += "$part "
        }

        return "$ConversationID: ($participantsString) Preview: $MostRecent"
//        return "$id"
    }

//    fun toHashMap(): HashMap<String, String> {
//        val hashMap = HashMap<String, String>()
//
//        hashMap["ConversationID"] = "$id"
//        hashMap["MostRecent"] = mostRecent
//
//        var participantsString = participants.fold("", {
//            acc: String, participant: String ->
//            participant
//        })
//
//        participantsString = "[$participantsString]"
//
//        hashMap["Participants"] = participantsString
//
//        return hashMap
//    }
}

class ConversationList(val Conversations: ArrayList<Conversation>) {
}