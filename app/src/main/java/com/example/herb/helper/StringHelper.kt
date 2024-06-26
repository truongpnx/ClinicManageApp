package com.example.herb.helper

import android.annotation.SuppressLint
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.Locale

interface StringHelper {

    fun numberToCurrency(number: Number, currencySymbols: String): String

    fun floatToString(number: Float, digitAfterPoint: Int): String

    fun numberToString(number: Number, prefix: String, postfix: String): String

    fun toProperCase(string: String): String

    companion object: StringHelper {
        override fun numberToCurrency(number: Number, currencySymbols: String): String {
            val decimalFormatSymbols = DecimalFormatSymbols().apply {
                currencySymbol = currencySymbols
                groupingSeparator = '.'
                decimalSeparator = ','

            }
            val decimalFormat = DecimalFormat.getCurrencyInstance() as DecimalFormat
            decimalFormat.decimalFormatSymbols = decimalFormatSymbols
            return decimalFormat.format(number)
        }

        @SuppressLint("DefaultLocale")
        override fun floatToString(number: Float, digitAfterPoint: Int): String {
            return String.format("%.${digitAfterPoint}f", number)
        }

        override fun numberToString(number: Number, prefix: String, postfix: String): String {
            return "${prefix}${number}${postfix}"
        }

        override fun toProperCase(string: String): String {
            return string.split(Regex("\\s+|[-_]")).joinToString(" ") { word ->
                word.lowercase().replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase() else it.toString()
                }
            }
        }
    }
}