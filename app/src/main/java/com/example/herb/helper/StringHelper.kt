package com.example.herb.helper

import kotlin.math.ceil
import kotlin.math.floor

interface StringHelper {

    fun numberToVND(number: Number): String

    fun numberToFloorWeight(number: Number, weightUnit: String): String

    fun numberToString(number: Number, prefix: String, postfix: String): String

    fun toProperCase(string: String): String

    companion object: StringHelper {
        override fun numberToVND(number: Number): String {
            return ceil(number.toFloat()).toInt().toString() + ".00 VND"
        }

        override fun numberToFloorWeight(number: Number, weightUnit: String): String {
            return floor(number.toFloat()).toInt().toString() + " ($weightUnit)"
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