package ru.sviridov.fintech.homework.lesson3.activity

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import ru.sviridov.fintech.homework.lesson3.R
import ru.sviridov.fintech.homework.lesson3.common.EXTRA_CONTACTS
import ru.sviridov.fintech.homework.lesson3.common.REQUEST_CODE_GET_READ_CONTACTS_PERMISSION
import ru.sviridov.fintech.homework.lesson3.common.SEND_BROADCAST_CONTACTS_LIST
import ru.sviridov.fintech.homework.lesson3.dto.Contact
import ru.sviridov.fintech.homework.lesson3.service.ContactsListIntentService


class SecondActivity : AppCompatActivity() {

    companion object {
        private val TAG = SecondActivity::class.simpleName
    }

    private val localBroadcastManager: LocalBroadcastManager by lazy {
        LocalBroadcastManager.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        if (!checkReadContactsPermission()) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_CONTACTS),
                REQUEST_CODE_GET_READ_CONTACTS_PERMISSION
            )
        } else {
            startIntentService()
        }
    }

    private var contactsReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d(TAG, "onReceive: intent is not null = ${intent != null}")
            intent?.let {
                setResult(RESULT_OK, it)
                finish()
            }
        }
    }
    override fun onStart() {
        super.onStart()
        localBroadcastManager
            .registerReceiver(contactsReceiver, IntentFilter(SEND_BROADCAST_CONTACTS_LIST))
    }

    override fun onStop() {
        super.onStop()
        localBroadcastManager.unregisterReceiver(contactsReceiver)
    }

    // Permission flow was simplified because it's a demo app, finishAffinity - just a way to handle negative case
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE_GET_READ_CONTACTS_PERMISSION -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                        this,
                        "Permission is necessary for continue working",
                        Toast.LENGTH_LONG
                    ).show()
                    finishAffinity()
                } else {
                    startIntentService()
                }
            }
        }
    }

    private fun startIntentService() {
        ContactsListIntentService.enqueueWork(
            this,
            Intent(this, ContactsListIntentService::class.java)
        )
    }

    private fun checkReadContactsPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED
    }
}