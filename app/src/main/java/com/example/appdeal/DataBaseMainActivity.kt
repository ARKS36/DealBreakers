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

        // Test for output
        Log.d("DB_RESULT", "Testing Logcat output...")
        //  Search for "eggs" sorted by price
        val priceResults = dbHelper.searchItems("eggs", "price")
        Log.d("DB_TEST", "Results for 'eggs' sorted by price:")
        priceResults.forEach { Log.d("DB_TEST", it) }

        //  Search for "eggs" sorted by distance
        val distanceResults = dbHelper.searchItems("eggs", "distance")
        Log.d("DB_TEST", "Results for 'eggs' sorted by distance:")
        distanceResults.forEach { Log.d("DB_TEST", it) }

        //  Search for "milk" sorted by price
        val milkPriceResults = dbHelper.searchItems("milk", "price")
        Log.d("DB_TEST", "Results for 'milk' sorted by price:")
        milkPriceResults.forEach { Log.d("DB_TEST", it) }

        //  Search for "bread" sorted by distance
        val breadDistanceResults = dbHelper.searchItems("bread", "distance")
        Log.d("DB_TEST", "Results for 'bread' sorted by distance:")
        breadDistanceResults.forEach { Log.d("DB_TEST", it) }
    }
}
