package br.com.pedrosilva.tecnonutri.ext

import android.content.Context
import androidx.annotation.StringRes
import br.com.pedrosilva.tecnonutri.R
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Date.format(context: Context, @StringRes pattern: Int = R.string.date_format): String =
    format(context.getString(pattern))

fun Date.format(pattern: String): String {
    val dateTimeFormat: DateFormat = SimpleDateFormat(pattern, Locale.getDefault())
    return dateTimeFormat.format(this)
}