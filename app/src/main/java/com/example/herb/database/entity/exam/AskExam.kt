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
data class AskExam(
    @PrimaryKey(autoGenerate = true) val askID: Int = 0,
    val examID: Int,
    val symptom: String,
)
