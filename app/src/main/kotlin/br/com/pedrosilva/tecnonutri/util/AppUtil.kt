package br.com.pedrosilva.tecnonutri.util

import android.util.TypedValue
import br.com.pedrosilva.tecnonutri.R
import br.com.pedrosilva.tecnonutri.util.ContextHelper.applicationContext
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object AppUtil {

    private const val PATTERN_NUMBER = "#.###"
    private const val PATTERN_KCAL = "#.#"
    private const val PATTERN_WEIGHT = "#.#"
    private const val PATTERN_MONEY = "#0.00"

    @Suppress("UNCHECKED_CAST")
    operator fun <T> get(element: Any): T = element as T

    fun formatDate(date: Date): String =
        formatDate(date, applicationContext!!.getString(R.string.date_format))

    private fun formatDate(date: Date, pattern: String): String {
        val dateTimeFormat: DateFormat =
            SimpleDateFormat(pattern, Locale.getDefault())
        return dateTimeFormat.format(date)
    }

    private fun formatDecimal(number: Number, pattern: String): String {
        val decimalFormat =
            get<DecimalFormat>(NumberFormat.getNumberInstance(Locale.getDefault()))
        decimalFormat.applyPattern(pattern)
        return decimalFormat.format(number)
    }

    fun formatDecimal(number: Number): String =
        formatDecimal(number, PATTERN_NUMBER)

    fun formatKcal(number: Number): String =
        formatDecimal(number, PATTERN_KCAL)

    fun formatWeight(number: Number): String =
        formatDecimal(number, PATTERN_WEIGHT)

    fun dp2Px(dp: Int): Int =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            500f,
            applicationContext!!.resources.displayMetrics
        ).toInt()
}