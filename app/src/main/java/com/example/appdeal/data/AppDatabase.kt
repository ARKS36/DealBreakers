package com.example.appdeal.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserDeal::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDealDao(): UserDealDao
}
