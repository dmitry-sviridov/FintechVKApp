package ru.sviridov.newsfeed.util

import android.content.res.Resources
import java.io.IOException

fun fromFile(fileName: String): String {
    var jsonString = ""
    try {
        val inputStream = Resources.getSystem().assets.open(fileName)
        jsonString = inputStream.use {
            it.readBytes().toString(Charsets.UTF_8)
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return jsonString
}