package com.example.appdeal.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDealDao {
    @Insert
    suspend fun insertUserDeal(deal: UserDeal)

    @Query("SELECT * FROM user_deals ORDER BY timestamp DESC")
    fun getAllDeals(): Flow<List<UserDeal>>
}
