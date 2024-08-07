package com.example.herb.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.herb.database.entity.Prescription
import com.example.herb.database.entity.StoredHerb
import kotlinx.coroutines.flow.Flow

@Dao
interface StoreHerbDao {
    @Upsert
    suspend fun upsertStoredHerb(storedHerb: StoredHerb)

    @Delete
    suspend fun deleteStoredHerb(storedHerb: StoredHerb)

    @Query("SELECT * FROM storedherb WHERE herbID = :herbID ORDER BY buyDate DESC ")
    fun getStoredHerbByIDOrderByDate(herbID: Int): Flow<List<StoredHerb>>

    @Query("SELECT * FROM storedherb WHERE herbID = :herbID ORDER BY buyPrice DESC ")
    fun getStoredHerbByIDOrderByBuyPrice(herbID: Int): Flow<List<StoredHerb>>

    @Query("SELECT * FROM storedherb WHERE herbID = :herbID ORDER BY buyWeight DESC ")
    fun getStoredHerbByIDOrderByBuyWeight(herbID: Int): Flow<List<StoredHerb>>

    @Query("SELECT * FROM storedherb WHERE herbID = :herbID ORDER BY storeWeight DESC ")
    fun getStoredHerbByIDOrderByStoreWeight(herbID: Int): Flow<List<StoredHerb>>

    @Query("SELECT A.preID, A.patientID " +
            "FROM prescription as A INNER JOIN prescriptionherb as B " +
            "ON A.preID = B.preID " +
            "WHERE B.herbID = :herbID")
    fun getPrescriptions(herbID: Int): Flow<List<Prescription>>

    @Query("SELECT * FROM storedherb WHERE storeID = :storeID")
    fun getStoredHerb(storeID: Int): StoredHerb
}