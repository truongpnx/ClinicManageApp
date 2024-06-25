package com.example.herb.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.herb.database.entity.Herb
import com.example.herb.database.entity.Prescription
import com.example.herb.database.entity.StoredHerb
import kotlinx.coroutines.flow.Flow

@Dao
interface StoreHerbDao {
    @Upsert
    suspend fun upsertStoredHerb(storedHerb: StoredHerb)

    @Delete
    suspend fun deleteStoredHerb(storedHerb: StoredHerb)

    @Query("SELECT * FROM storedherb WHERE storeID = :storeID ORDER BY buyDate DESC ")
    fun getStoredHerbByIDOrderByDate(storeID: Int): Flow<List<StoredHerb>>

    @Query("SELECT * FROM storedherb WHERE storeID = :storeID ORDER BY buyPrice DESC ")
    fun getStoredHerbByIDOrderByBuyPrice(storeID: Int): Flow<List<StoredHerb>>

    @Query("SELECT * FROM storedherb WHERE storeID = :storeID ORDER BY buyDate DESC ")
    fun getStoredHerbByIDOrderByBuyWeight(storeID: Int): Flow<List<StoredHerb>>

    @Query("SELECT * FROM storedherb WHERE storeID = :storeID ORDER BY buyDate DESC ")
    fun getStoredHerbByIDOrderByStoreWeight(storeID: Int): Flow<List<StoredHerb>>

    @Query("SELECT A.preID, A.patientID " +
            "FROM prescription as A INNER JOIN prescriptionherb as B " +
            "ON A.preID = B.preID " +
            "WHERE A.preID = :preID")
    fun getPrescriptions(preID: Int): Flow<List<Prescription>>

    @Query("SELECT * FROM storedherb WHERE storeID = :storeID")
    fun getStoredHerb(storeID: Int): StoredHerb
}