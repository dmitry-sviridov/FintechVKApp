package ru.sviridov.fintech.homework.lesson3.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.li_contact.view.*
import ru.sviridov.fintech.homework.lesson3.R
import ru.sviridov.fintech.homework.lesson3.dto.Contact

class ContactListAdapter(private val list: List<Contact>)
    : RecyclerView.Adapter<ContactListViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ContactListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.li_contact, parent, false)
        return ContactListViewHolder(itemView, parent)
    }

    override fun onBindViewHolder(holder: ContactListViewHolder, position: Int)
            = holder.bind(list[position])

    override fun getItemCount(): Int = list.size
}

class ContactListViewHolder(itemView: View, parent: ViewGroup) :
    RecyclerView.ViewHolder(itemView) {

    fun bind(contact: Contact) {
        itemView.tvContactName.text = contact.name
        itemView.tvContactPhoneNum.text = contact.phoneNum
    }
}