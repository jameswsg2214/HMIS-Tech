package com.hmis_tn.lims.utils

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.collection.LruCache
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.hmis_tn.lims.R
import com.hmis_tn.lims.application.HmisApplication.Companion.CHANNEL_1
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

class FileHelper(private val context: Context?) {

    companion object {
        const val PARENT_FOLDER = "HMIS"
    }

    private var mFile: File? = null


    fun showNotification(file: File) {
        val path = FileProvider.getUriForFile(
            context!!,
            context.applicationContext.packageName + ".provider",
            file
        )
        val intent = Intent(Intent.ACTION_VIEW)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        val chooser = Intent.createChooser(intent, "Open")
        intent.setDataAndType(path, "application/pdf")

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationManagerCompat = NotificationManagerCompat.from(context)

        val notification = NotificationCompat.Builder(context, CHANNEL_1)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Downloaded successfully")
            .setContentText(file.path)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .build()

        notificationManagerCompat.notify(1, notification)
    }
}