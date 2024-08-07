package com.example.herb.helper

import android.annotation.SuppressLint
import android.util.Log
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

interface StringHelper {

    fun numberToFormattedString(number: Number, pattern: String = "#,###.##"): String

    fun floatToString(number: Float, digitAfterPoint: Int): String

    fun numberToString(number: Number, prefix: String, postfix: String): String

    fun toProperCase(string: String): String

    fun stringToNumber(string: String, pattern: String = "#,###.##"): Number

    fun splitStringFloatPattern(string: String): Pair<String, String>

    companion object : StringHelper {
        override fun numberToFormattedString(number: Number, pattern: String): String {
            val decimalFormatSymbols = DecimalFormatSymbols().apply {
                groupingSeparator = ','
                decimalSeparator = '.'
            }
            val decimalFormat = DecimalFormat(pattern, decimalFormatSymbols)
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

        override fun stringToNumber(string: String, pattern: String): Number {
            val decimalFormatSymbols = DecimalFormatSymbols().apply {
                groupingSeparator = ','
                decimalSeparator = '.'
            }
            val decimalFormat = DecimalFormat(pattern, decimalFormatSymbols)
            return decimalFormat.parse(string) ?: 0
        }

        override fun splitStringFloatPattern(string: String): Pair<String, String> {
            val regex = """(\d*)(\.(\d*))?""".toRegex()
            val matchResult = regex.matchEntire(string)
            return if (matchResult != null) {
                Log.d("splitStringFloatPattern: ", matchResult.groupValues.joinToString(","))
                val naturalPart = matchResult.groupValues[1].ifEmpty { "" }
                val decimalPart = matchResult.groupValues[3].ifEmpty { "" }
                Pair(naturalPart, decimalPart)
            } else {
                Pair("", "")
            }
        }

    }
}