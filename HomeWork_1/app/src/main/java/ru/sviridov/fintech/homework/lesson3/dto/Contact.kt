package ru.sviridov.fintech.homework.lesson3.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Contact(val id: String, val name: String?, val phoneNum: String?) : Parcelable
