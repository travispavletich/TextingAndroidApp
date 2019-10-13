package com.example.textingapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var x = 0

//        counterButton.setOnClickListener {
//            x++
//            counterText.setText("Counter: " + x)
//        }

        submitIPButton.setOnClickListener {
            Intent(this, TextingAppService::class.java).also { intent ->
                Log.d("Intent", "startService")
                TextingAppService.SERVER_IP = serverIPInput.text.toString()
                startService(intent)
            }
        }

        PermissionHandler.requestInternetPermission(this)
        PermissionHandler.requestPermissionToSendSMS(this)
        PermissionHandler.requestPermissionToReadSMS(this)
        PermissionHandler.requestPermissionToReadMMS(this)

    }
}