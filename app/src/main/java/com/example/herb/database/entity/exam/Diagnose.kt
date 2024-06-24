package com.example.herb.database.entity.exam

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.herb.database.entity.Examination

@Entity(
    foreignKeys = [
        ForeignKey(Examination::class, parentColumns = ["examID"], childColumns = ["examID"]),
    ],
    indices = [Index(value = ["examID"])]
)
data class Diagnose(
    @PrimaryKey(autoGenerate = true) val diagnoseID: Int = 0,
    val examID: Int,
    // I asked doctor for these columns
    val theory: String, // Ly
    val solution: String, // Phap
    val how: String, // Phuong
)
