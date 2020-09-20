package ru.sviridov.fintech.homework.lesson3.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import kotlinx.android.synthetic.main.activity_main.*
import ru.sviridov.fintech.homework.lesson3.R
import ru.sviridov.fintech.homework.lesson3.adapter.ContactListAdapter
import ru.sviridov.fintech.homework.lesson3.common.EXTRA_CONTACTS
import ru.sviridov.fintech.homework.lesson3.common.REQUEST_CODE_GET_CONTACTS
import ru.sviridov.fintech.homework.lesson3.dto.Contact

class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = MainActivity::class.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startSecondActivity()
    }

    private fun startSecondActivity() {
        val intent = Intent(this, SecondActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE_GET_CONTACTS)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_GET_CONTACTS) {
            Log.d(TAG, "onActivityResult: Correct request code")
            if (resultCode == RESULT_OK) {
                val contacts = data?.getParcelableArrayListExtra<Contact>(EXTRA_CONTACTS)
                Log.d(TAG, "onActivityResult: Result code = OK, Data = $contacts")

                if (!contacts.isNullOrEmpty()) {
                    tv_contacts_list_empty.visibility = View.GONE
                    rv_contacts.apply {
                        layoutManager = LinearLayoutManager(this@MainActivity)
                        adapter = ContactListAdapter(contacts)
                    }
                }

            }
        }
    }
}