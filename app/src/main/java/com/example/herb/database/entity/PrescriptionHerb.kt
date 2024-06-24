package com.example.herb.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index


@Entity(
    primaryKeys = ["preID", "herbID"],
    foreignKeys = [
        ForeignKey(Prescription::class, parentColumns = ["preID"], childColumns = ["preID"]),
        ForeignKey(Herb::class, parentColumns = ["herbID"], childColumns = ["herbID"])
    ],
    indices = [Index(value = ["preID"]), Index(value = ["herbID"])]
)
data class PrescriptionHerb (
    val preID: Int,
    val herbID: Int,
    val useWeight: Float,
    val numberPack: Int,
    val cost: Long
)