package com.example.footballapps.utils

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat.startActivity
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.example.footballapps.R
import org.jetbrains.anko.toast
import java.text.SimpleDateFormat
import java.util.*

fun emptyString() = ""

fun dateConverter(context: Context, date: String, time: String): String {
    val oldDate = "$date $time"

    var newDate = emptyString()
    val apiFormat: SimpleDateFormat
    val newFormat: SimpleDateFormat

    if (date.isNotEmpty() && time.isNotEmpty()) {
        when {
            date.isEmpty() -> {
                apiFormat =
                    SimpleDateFormat(
                        context.getString(R.string.format_date_api_empty_date),
                        Locale.getDefault()
                    )
                newFormat =
                    SimpleDateFormat(
                        context.getString(R.string.format_date_new_empty_date),
                        Locale.getDefault()
                    )

            }
            time.isEmpty() -> {
                apiFormat =
                    SimpleDateFormat(
                        context.getString(R.string.format_date_api_empty_time),
                        Locale.getDefault()
                    )
                newFormat =
                    SimpleDateFormat(
                        context.getString(R.string.format_date_new_empty_time),
                        Locale.getDefault()
                    )
            }
            else -> {
                apiFormat =
                    SimpleDateFormat(
                        context.getString(R.string.format_date_api),
                        Locale.getDefault()
                    )
                newFormat =
                    SimpleDateFormat(
                        context.getString(R.string.format_date_new),
                        Locale.getDefault()
                    )
            }
        }

        apiFormat.timeZone = TimeZone.getTimeZone("GMT")
        val apiDate = apiFormat.parse(oldDate) ?: Date()

        newFormat.timeZone = TimeZone.getTimeZone("Asia/Jakarta")

        newDate = newFormat.format(apiDate)
    }

    return newDate
}

fun AppCompatActivity.setupToolbar(
    toolbar: Toolbar?,
    title: String = emptyString(),
    isChild: Boolean = false
) {
    toolbar?.let {
        setSupportActionBar(it)

        if (!isChild) {
            it.navigationIcon = null
        }
    }

    if (supportActionBar != null) {
        supportActionBar!!.title = title
        supportActionBar!!.setDisplayHomeAsUpEnabled(isChild)
    }
}

fun circularProgressBar(context: Context): Drawable {
    val circularProgressDrawable = CircularProgressDrawable(context)
    circularProgressDrawable.strokeWidth = 5f
    circularProgressDrawable.centerRadius = 30f
    circularProgressDrawable.start()
    return circularProgressDrawable
}

fun openWebPage(context: Context, url: String) {
    if (url.isNotEmpty()) {

        val link = String.format(context.getString(R.string.format_url), url)
        val webpage: Uri = Uri.parse(link)
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        }
    } else {

        context.toast(context.getString(R.string.label_no_url))
    }

}

