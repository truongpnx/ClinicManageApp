package com.example.herb.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(Herb::class, parentColumns = ["herbID"], childColumns = ["herbID"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index(value = ["herbID"])]
)
data class StoredHerb(
    @PrimaryKey(autoGenerate = true) val storeID: Int = 0,
    val herbID: Int,
    // when buy herb
    val buyDate: Long,
    val buyPrice: Long,
    val buyWeight: Float,

    // when preprocess herb and store
    val processTime: Float,
    val laborCost: Long,
    val storeWeight: Float,
    val additionalCost: Long,
    val isImport: Boolean
)

