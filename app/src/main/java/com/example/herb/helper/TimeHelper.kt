package com.example.herb.helper

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

interface TimeHelper {

    fun convertToUTC(date: Date, sourceTimeZone: ZoneId): Date

    fun utcToZone(date: Date, targetZone: ZoneId): Date

    fun utilDateToSqlLong(date: Date): Long

    fun longToUtilDate(dateLong: Long): Date

    fun localDateToString(date: Date, format: String = "HH:mm dd/MM/yyyy"): String

    fun localDateClockToDate(localDate: LocalDate, localTime: LocalTime): Date

    fun timeToString(localTime: LocalTime, format: String): String

    fun localDateToString(date: LocalDate, format: String = "dd/MM/yyyy"): String

    companion object: TimeHelper {
        override fun convertToUTC(date: Date, sourceTimeZone: ZoneId): Date {
            // Step 1: Convert java.util.Date to Instant
            val instant = date.toInstant()

            // Step 2: Convert Instant to ZonedDateTime in the source time zone (UTC+7)
            val sourceZonedDateTime = instant.atZone(sourceTimeZone)

            // Step 3: Convert ZonedDateTime to ZonedDateTime in UTC
            val utcZonedDateTime = sourceZonedDateTime.withZoneSameInstant(ZoneId.of("UTC"))

            // Step 4: Convert ZonedDateTime back to Instant
            val utcInstant = utcZonedDateTime.toInstant()

            // Step 5: Convert Instant back to java.util.Date
            return Date.from(utcInstant)
        }

        override fun utcToZone(date: Date, targetZone: ZoneId): Date {
            val instant = date.toInstant()

            val utcZonedDateTime = instant.atZone(ZoneId.of("UTC"))

            val targetZonedDateTime = utcZonedDateTime.withZoneSameInstant(targetZone)

            val targetInstant = targetZonedDateTime.toInstant()

            // Convert Instant back to java.util.Date
            return Date.from(targetInstant)
        }

        override fun utilDateToSqlLong(date: Date): Long {
            return date.time
        }

        override fun longToUtilDate(dateLong: Long): Date {
            return Date(dateLong)
        }

        override fun localDateToString(date: Date, format: String): String {
            val dateFormat = SimpleDateFormat(format, Locale.getDefault())
            return dateFormat.format(date)
        }

        override fun localDateToString(date: LocalDate, format: String): String {
            val dateFormat = DateTimeFormatter.ofPattern(format, Locale.getDefault())
            return date.format(dateFormat)
        }

        override fun localDateClockToDate(localDate: LocalDate, localTime: LocalTime): Date {
            val localDateTime = LocalDateTime.of(localDate, localTime)
            return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant())
        }

        override fun timeToString(localTime: LocalTime, format: String): String {
            val formatter = DateTimeFormatter.ofPattern(format, Locale.getDefault())
            return localTime.format(formatter)
        }
    }

}