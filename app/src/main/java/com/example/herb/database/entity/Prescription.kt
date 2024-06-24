package com.example.herb.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(Patient::class, parentColumns = ["patientID"], childColumns = ["patientID"])
    ],
    indices = [Index(value = ["patientID"])]
)
data class Prescription(
    @PrimaryKey(autoGenerate = true) val preID: Int = 0,
    val patientID: Int,
)

