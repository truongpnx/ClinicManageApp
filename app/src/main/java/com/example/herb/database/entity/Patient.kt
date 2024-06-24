package com.example.herb.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["profileName"], unique = true)])
data class Patient(
    @PrimaryKey(autoGenerate = true) val patientID: Int = 0,
    val profileName: String,
    val lastName: String, // ho
    val firstName: String, // ten
    val bornYear: Int,
    val sex: String,
    val address: String,
    val phoneNumber: String,
)
