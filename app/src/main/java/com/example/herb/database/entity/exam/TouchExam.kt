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
data class TouchExam(
    @PrimaryKey(autoGenerate = true) val touchID: Int = 0,
    val examID: Int,
    // I asked doctor for these columns
    val rightT: String,
    val rightQ: String,
    val rightX: String,
    val leftT: String,
    val leftQ: String,
    val leftX: String,
)
