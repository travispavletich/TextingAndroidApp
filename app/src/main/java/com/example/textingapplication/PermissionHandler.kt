package com.example.textingapplication

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.content.ContextCompat

class PermissionHandler
{
    companion object {
        const val ACCESS_INTERNET_REQUEST = 1
        const val SEND_SMS_REQUEST = 1
        const val READ_SMS_REQUEST = 1
        const val RECEIVE_SMS_REQUEST = 1
        const val RECEIVE_MMS_REQUEST = 1
        const val READ_CONTACT_REQUEST = 1

        fun requestInternetPermission(activity: Activity) {
            if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.INTERNET)
                    != PackageManager.PERMISSION_GRANTED) {
                if (activity.shouldShowRequestPermissionRationale(android.Manifest.permission.INTERNET)) {
                    Toast.makeText(activity, "Please allow permission", Toast.LENGTH_SHORT).show()
                }

                activity.requestPermissions(Array(1) { android.Manifest.permission.INTERNET},
                                            ACCESS_INTERNET_REQUEST)
            }
        }

        fun requestPermissionToSendSMS(activity: Activity) {
            if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.SEND_SMS)
                    != PackageManager.PERMISSION_GRANTED) {
                if (activity.shouldShowRequestPermissionRationale(android.Manifest.permission.SEND_SMS)) {
                    Toast.makeText(activity, "Please allow permission", Toast.LENGTH_SHORT).show()
                }

                activity.requestPermissions(Array(1) { android.Manifest.permission.SEND_SMS },
                                            SEND_SMS_REQUEST)
            }
        }

        fun requestPermissionToReadSMS(activity: Activity) {
            if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {
                if (activity.shouldShowRequestPermissionRationale(android.Manifest.permission.READ_SMS)) {
                    Toast.makeText(activity, "Please allow permission", Toast.LENGTH_SHORT).show()
                }

                activity.requestPermissions(Array(1) { android.Manifest.permission.READ_SMS },
                    READ_SMS_REQUEST)
            }
        }

        fun requestPermissionToReceiveSMS(activity: Activity) {
            if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED) {
                if (activity.shouldShowRequestPermissionRationale(android.Manifest.permission.RECEIVE_SMS)) {
                    Toast.makeText(activity, "Please allow permission", Toast.LENGTH_SHORT).show()
                }

                activity.requestPermissions(Array(1) { android.Manifest.permission.RECEIVE_SMS },
                    RECEIVE_SMS_REQUEST)
            }
        }

        fun requestPermissionToReadMMS(activity: Activity) {
            if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.RECEIVE_MMS)
                != PackageManager.PERMISSION_GRANTED) {
                if (activity.shouldShowRequestPermissionRationale(android.Manifest.permission.RECEIVE_MMS)) {
                    Toast.makeText(activity, "Please allow permission", Toast.LENGTH_SHORT).show()
                }

                activity.requestPermissions(Array(1) { android.Manifest.permission.RECEIVE_MMS },
                    RECEIVE_MMS_REQUEST)
            }
        }

        fun requestPermissionToReadContacts(activity: Activity) {
            if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
                if (activity.shouldShowRequestPermissionRationale(android.Manifest.permission.READ_CONTACTS)) {
                    Toast.makeText(activity, "Please allow permission", Toast.LENGTH_SHORT).show()
                }

                activity.requestPermissions(Array(1) { android.Manifest.permission.READ_CONTACTS },
                    READ_CONTACT_REQUEST)
            }
        }
    }
}