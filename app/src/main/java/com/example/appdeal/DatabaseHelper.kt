package com.example.appdeal  // ✅ Only one package declaration

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "ItemsDB"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "items"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_STORE = "store_name"
        private const val COLUMN_PRICE = "price"
        private const val COLUMN_UNIT = "unit"
        private const val COLUMN_PRICE_PER_UNIT = "price_per_unit"
        private const val COLUMN_DISTANCE = "distance"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableStatement = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT,
                $COLUMN_STORE TEXT,
                $COLUMN_PRICE REAL,
                $COLUMN_UNIT TEXT,
                $COLUMN_PRICE_PER_UNIT REAL,
                $COLUMN_DISTANCE REAL
            )
        """.trimIndent()
        db.execSQL(createTableStatement)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // ✅ Insert item method - Place it here
    fun insertItem(name: String, storeName: String, price: Double, unit: String, pricePerUnit: Double, distance: Double) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_STORE, storeName)
            put(COLUMN_PRICE, price)
            put(COLUMN_UNIT, unit)
            put(COLUMN_PRICE_PER_UNIT, pricePerUnit)
            put(COLUMN_DISTANCE, distance)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }
}
