package com.example.okl_app_mvvm.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SigningDAO {

    @Insert
    suspend fun insert(signingEntity: SigningEntity)

    @Update
    suspend fun update(signingEntity: SigningEntity)

    @Delete
    suspend fun delete(signingEntity: SigningEntity)


    @Query("SELECT * FROM `flowers-table`")
    fun fetchAllFlowers(): Flow<List<SigningEntity>>


}