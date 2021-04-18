package br.com.pedrosilva.tecnonutri.ext

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

private const val DEFAULT_DECIMAL_PATTERN = "#.###"
private const val NUMBER_KCAL_PATTERN = "#.#"
private const val NUMBER_WEIGHT_PATTERN = "#.#"

fun Number.formatDecimal(pattern: String = DEFAULT_DECIMAL_PATTERN): String? {
    val decimalFormat = NumberFormat.getNumberInstance(Locale.getDefault()) as? DecimalFormat
    decimalFormat?.applyPattern(pattern)
    return decimalFormat?.format(this)
}

fun Number.formatKcal(): String? =
    formatDecimal(NUMBER_KCAL_PATTERN)

fun Number.formatWeight(): String? =
    formatDecimal(NUMBER_WEIGHT_PATTERN)