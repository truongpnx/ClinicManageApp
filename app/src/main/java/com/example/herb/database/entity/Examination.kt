package com.example.herb.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

// Examination means store all data of patient

@Entity(
    foreignKeys = [
        ForeignKey(Patient::class, parentColumns = ["patientID"], childColumns = ["patientID"]),
    ],
    indices = [Index(value = ["patientID"])]
)
data class Examination(
    @PrimaryKey(autoGenerate = true) val examID: Int = 0,
    val patientID: Int,
    val reason: String,
    val prehistoric: String,
)
