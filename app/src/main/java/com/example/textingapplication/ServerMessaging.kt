package com.example.textingapplication

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONStringer

class ServerMessaging {
    companion object {
        private val baseUrl = "http://${TextingAppService.SERVER_IP}:5000"

        fun sendFirebaseToken(context: Context) {
            var token = TextingAppService.TOKEN

            Log.d("Token", "token=$token")

            val url = "$baseUrl/Android/token?token=$token"

            sendHttpRequest(url, context)
        }

        fun sendConversationList(context: Context) {
            val conversations = MessageHandler.getConversations(context)
            val url = "$baseUrl/Android/ConversationList"

            var conversationArray = ArrayList<Conversation>()

            for (conversation in conversations) {
                conversationArray.add(conversation)
            }

            val conversationList = ConversationList(conversationArray)

            var gson = Gson()

            val conversationsJSONString = gson.toJson(conversationList)

            Log.d("JSON", conversationsJSONString)

            val conversationsJSON = JSONObject(conversationsJSONString)

            Log.d("JSON", conversationsJSON.getString(("Conversations")))

            val conversationListRequest = JsonObjectRequest(
                Request.Method.POST, url, conversationsJSON,
                Response.Listener {
                    response ->
                    Log.d("HttpResponse", "$response")
                },
                Response.ErrorListener {
                    error ->
                    Log.d("HttpError", "$error")
                }
            )

            val queue = Volley.newRequestQueue(context)
            queue.add(conversationListRequest)
        }

        fun sendHttpRequest(url: String, context: Context) {
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
                Log.d("Error", "No internet permission")
            }
            val queue = Volley.newRequestQueue(context)

            Log.d("HttpRequest", "Sending request...")
            val stringRequest = StringRequest(
                Request.Method.GET, url,
                Response.Listener<String> { response ->
                    Log.d("HttpResponse", "Response: $response")
                },
                Response.ErrorListener { Log.d("HttpError", "Didn't work") })

            queue.add(stringRequest)
        }
    }
}