package ru.sviridov.fintech.homework.lesson3.service

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.provider.ContactsContract
import android.util.Log
import androidx.core.app.JobIntentService
import androidx.core.database.getStringOrNull
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import ru.sviridov.fintech.homework.lesson3.common.EXTRA_CONTACTS
import ru.sviridov.fintech.homework.lesson3.common.JOB_ID_GET_CONTACTS
import ru.sviridov.fintech.homework.lesson3.common.SEND_BROADCAST_CONTACTS_LIST
import ru.sviridov.fintech.homework.lesson3.dto.Contact

class ContactsListIntentService : JobIntentService() {

    companion object {
        private val TAG = ContactsListIntentService::class.simpleName

        @JvmStatic
        fun enqueueWork(ctx: Context, work: Intent) {
            enqueueWork(ctx, ContactsListIntentService::class.java, JOB_ID_GET_CONTACTS, work)
        }
    }

    override fun onHandleWork(intent: Intent) {
        Log.d(TAG, "onHandleWork")
        val contacts = getContacts()
        Log.d(TAG, "workDone: contacts - $contacts, starting sending broadcast")
        val intent = Intent(SEND_BROADCAST_CONTACTS_LIST).putExtra(EXTRA_CONTACTS, contacts)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @SuppressLint("Recycle")
    private fun getContacts(): ArrayList<Contact> {
        val contactList = arrayListOf<Contact>()

        val uri = ContactsContract.Contacts.CONTENT_URI
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.let {
            Log.d(TAG, "getContacts: count = ${it.count}")

            it.moveToPosition(-1) // set cursor before the first row

            while (it.moveToNext()) {
                val id = it.getString(it.getColumnIndex(ContactsContract.Contacts._ID))
                val displayName = it.getStringOrNull(
                        it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                var phoneNumber: String? = null

                if (it.getStringOrNull(
                                it.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) == "1") {
                    phoneNumber = readPhoneNumber(id)
                }
                contactList.add(Contact(id, displayName, phoneNumber))
            }
        }
        return contactList
    }

    @SuppressLint("Recycle")
    private fun readPhoneNumber(contactId: String): String? {
        var result: String? = null
        val numbers = contentResolver
                .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                        null,
                        null
                )
        numbers?.let {
            while (it.moveToNext()) {
                result = it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            }
        }
        return result
    }
}