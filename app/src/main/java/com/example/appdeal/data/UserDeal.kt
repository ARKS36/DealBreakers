package com.example.appdeal.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "user_deals")
data class UserDeal(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val itemName: String,
    val storeName: String,
    val price: Double,
    val quantity: String?,
    val unit: String?,
    val timestamp: Long = System.currentTimeMillis()
)
