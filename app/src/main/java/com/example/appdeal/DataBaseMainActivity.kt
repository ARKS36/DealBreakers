package com.example.appdeal

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class DataBaseMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the database
        val dbHelper = DatabaseHelper(this)

        // Insert sample items
        dbHelper.insertItem("eggs", "Eggsmarket", 5.40, "lb", 2.70, 0.4)
        dbHelper.insertItem("eggs", "Freshmart", 4.80, "lb", 2.40, 0.6)
        dbHelper.insertItem("eggs", "LocalGrocer", 6.20, "lb", 3.10, 0.3)
        dbHelper.insertItem("milk", "MilkMart", 3.20, "gal", 3.20, 0.5)
        dbHelper.insertItem("milk", "TraderMart", 3.75, "gal", 3.10, 0.7)
        dbHelper.insertItem("milk", "ShopSmart", 2.95, "gal", 2.95, 0.6)
        dbHelper.insertItem("bread", "ShopSmart", 1.85, "lb", 1.85, 0.6)
        dbHelper.insertItem("bread", "TraderMart", 2.30, "lb", 2.30, 0.7)
        dbHelper.insertItem("bread", "QuickShop", 1.60, "lb", 1.60, 0.9)

        // Search for items sorted by price
        val priceResults = dbHelper.searchItems("eggs", "price")
        priceResults.forEach { Log.d("DB_RESULT", it) }

        // Search for items sorted by distance
        val distanceResults = dbHelper.searchItems("eggs", "distance")
        distanceResults.forEach { Log.d("DB_RESULT", it) }
    }
}
