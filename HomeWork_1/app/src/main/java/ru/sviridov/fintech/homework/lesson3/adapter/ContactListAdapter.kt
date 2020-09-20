package ru.sviridov.fintech.homework.lesson3.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.sviridov.fintech.homework.lesson3.R
import ru.sviridov.fintech.homework.lesson3.dto.Contact

class ContactListAdapter(private val list: List<Contact>)
    : RecyclerView.Adapter<ContactListViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ContactListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ContactListViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: ContactListViewHolder, position: Int)
            = holder.bind(list[position])

    override fun getItemCount(): Int = list.size
}

class ContactListViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.li_contact, parent, false)) {

    private var nameView: TextView
    private var phoneView: TextView

    init {
        nameView = itemView.findViewById(R.id.tv_contact_name)
        phoneView = itemView.findViewById(R.id.tv_contact_phone)
    }

    fun bind(contact: Contact) {
        nameView.text = contact.name
        phoneView.text = contact.phoneNum
    }
}