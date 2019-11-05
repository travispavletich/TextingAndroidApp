package com.example.textingapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseApp.initializeApp(this)

        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener(OnCompleteListener {
            task ->
            val token = task.result!!.token

            TextingAppService.TOKEN = token
        })

        submitIPButton.setOnClickListener {
            Intent(this, TextingAppService::class.java).also { intent ->
                Log.d("Intent", "startService")
                TextingAppService.SERVER_IP = serverIPInput.text.toString()
                startService(intent)
            }
        }

        Intent(this, SmsListener::class.java).also {
            Log.d("Intent", "start SMS Listener")
            startService(it)
        }

        PermissionHandler.requestInternetPermission(this)
        PermissionHandler.requestPermissionToSendSMS(this)
        PermissionHandler.requestPermissionToReadSMS(this)
        PermissionHandler.requestPermissionToReadMMS(this)
        PermissionHandler.requestPermissionToReceiveSMS(this)

    }
}