package com.example.liveattendanceapp.dialog

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.example.liveattendanceapp.R

object MyDialog {
    private var dialogBuilder: AlertDialog? = null

    fun dynamicDialog(context: Context?, title: String, message: String){
        dialogBuilder = AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .show()
    }

    @SuppressLint("InflateParams")
    fun showProgressDialog(context: Context?){
        val dialogView = LayoutInflater.from(context).inflate(R.layout.layout_progress, null) //tampilna progress dengan menggunakan layout_progress.xml
        dialogBuilder = AlertDialog.Builder(context)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        //hapus background, atau buat transparan dari drawable dari layout_progress.xml
        dialogBuilder?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialogBuilder?.show()
    }

    fun hideDialog(){
        if (dialogBuilder != null){
            dialogBuilder?.dismiss()
        }
    }
}