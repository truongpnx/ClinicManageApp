package com.example.herb.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    indices = [Index(value = ["herbName"], unique = true)]
)
data class Herb(
    @PrimaryKey(autoGenerate = true) val herbID: Int = 0,
    val herbName: String,
    val avgPrice: Long = 0,
    val totalWeight: Float = 0f,
)
