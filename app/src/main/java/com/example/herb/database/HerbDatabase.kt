package com.example.herb.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.herb.database.dao.HerbDao
import com.example.herb.database.dao.StoreHerbDao
import com.example.herb.database.entity.Examination
import com.example.herb.database.entity.Herb
import com.example.herb.database.entity.Patient
import com.example.herb.database.entity.Prescription
import com.example.herb.database.entity.PrescriptionHerb
import com.example.herb.database.entity.StoredHerb
import com.example.herb.database.entity.exam.AskExam
import com.example.herb.database.entity.exam.Diagnose
import com.example.herb.database.entity.exam.HearExam
import com.example.herb.database.entity.exam.TouchExam
import com.example.herb.database.entity.exam.WatchExam

@Database(
    entities = [
        Herb::class,
        StoredHerb::class,
        Prescription::class,
        PrescriptionHerb::class,
        Patient::class,
        Examination::class,
        AskExam::class,
        Diagnose::class,
        HearExam::class,
        TouchExam::class,
        WatchExam::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class HerbDatabase : RoomDatabase() {
    // TODO: add dao to database
    abstract fun herbDao(): HerbDao
    abstract fun storedHerbDao(): StoreHerbDao
}