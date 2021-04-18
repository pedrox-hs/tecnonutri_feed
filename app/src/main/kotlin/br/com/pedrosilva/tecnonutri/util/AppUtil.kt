package br.com.pedrosilva.tecnonutri.util

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

object AppUtil {

    private const val PATTERN_NUMBER = "#.###"
    private const val PATTERN_KCAL = "#.#"
    private const val PATTERN_WEIGHT = "#.#"
    private const val PATTERN_MONEY = "#0.00"

    @Suppress("UNCHECKED_CAST")
    operator fun <T> get(element: Any): T = element as T

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
}