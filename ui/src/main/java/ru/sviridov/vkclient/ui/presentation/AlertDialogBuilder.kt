package ru.sviridov.vkclient.ui.presentation

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog

object AlertDialogBuilder {

    fun showDialog(
        context: Context, title: String, msg: String,
        positiveBtnText: String, negativeBtnText: String?,
        positiveBtnClickListener: DialogInterface.OnClickListener,
        negativeBtnClickListener: DialogInterface.OnClickListener?
    ): AlertDialog {
        val builder = AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(msg)
            .setCancelable(true)
            .setPositiveButton(positiveBtnText, positiveBtnClickListener)
        if (negativeBtnText != null)
            builder.setNegativeButton(negativeBtnText, negativeBtnClickListener)
        val alert = builder.create()
        alert.show()
        return alert
    }
}