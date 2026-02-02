package com.nassdk.tradernet.core.formatter

import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

internal class AmountFormatter {

    private val symbols = DecimalFormatSymbols(Locale.getDefault()).apply {
        decimalSeparator = DECIMAL_SEPARATOR
        groupingSeparator = GROUPING_SEPARATOR
    }

    private val defaultFormat = DecimalFormat("#,##0.##", symbols)

    fun format(value: BigDecimal?, minStep: BigDecimal? = null): String {
        if (value == null) return DASH

        val roundedValue = if (minStep != null && minStep > BigDecimal.ZERO) {
            val divided = value.divide(minStep, 0, java.math.RoundingMode.HALF_UP)
            divided.multiply(minStep)
        } else {
            value
        }

        val scale = if (minStep != null && minStep > BigDecimal.ZERO) {
            minStep.stripTrailingZeros().scale().coerceAtLeast(0)
        } else {
            val strippedValue = roundedValue.stripTrailingZeros()
            if (strippedValue.scale() <= 0) {
                2
            } else {
                strippedValue.scale()
            }
        }

        val format = createFormatForScale(scale)
        return format.format(roundedValue)
    }

    fun formatWithSign(value: BigDecimal?, minStep: BigDecimal? = null): String {
        if (value == null) return DASH
        val formatted = format(value, minStep)
        return when {
            value > BigDecimal.ZERO -> "$PLUS$formatted"
            else -> formatted
        }
    }

    fun formatPercent(value: BigDecimal?): String {
        if (value == null) return DASH
        val formatted = format(value)
        return when {
            value > BigDecimal.ZERO -> "$PLUS$formatted$PERCENT"
            else -> "$formatted$PERCENT"
        }
    }

    private fun createFormatForScale(scale: Int): DecimalFormat {
        if (scale <= 0) return defaultFormat

        val pattern = "#,##0." + "0".repeat(scale)
        return DecimalFormat(pattern, symbols)
    }

    companion object {
        private const val PLUS = "+"
        private const val DASH = "-"
        private const val PERCENT = "%"
        private const val DECIMAL_SEPARATOR = '.'
        private const val GROUPING_SEPARATOR = ' '
    }
}
