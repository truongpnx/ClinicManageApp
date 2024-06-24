package com.example.herb.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import com.example.herb.database.entity.Herb
import kotlinx.coroutines.flow.Flow

@Dao
interface HerbDao {

    @Upsert()
    suspend fun upsertHerb(herb: Herb)

    @Delete
    suspend fun deleteHerb(herb: Herb)

    @Query("SELECT * FROM herb WHERE herbName LIKE '%' || :stringQuery || '%' ORDER BY herbID ASC")
    fun getHerbsHasStringOrderByID(stringQuery: String): Flow<List<Herb>>

    @Query("SELECT * FROM herb WHERE herbName LIKE '%' || :stringQuery || '%' ORDER BY herbName ASC")
    fun getHerbsHasStringOrderByName(stringQuery: String): Flow<List<Herb>>

    @Query("SELECT * FROM herb WHERE herbName LIKE '%' || :stringQuery || '%' ORDER BY avgPrice ASC")
    fun getHerbsHasStringOrderByAvgPrice(stringQuery: String): Flow<List<Herb>>

    @Query("SELECT * FROM herb WHERE herbName LIKE '%' || :stringQuery || '%' ORDER BY totalWeight ASC")
    fun getHerbsHasStringOrderByWeight(stringQuery: String): Flow<List<Herb>>
}