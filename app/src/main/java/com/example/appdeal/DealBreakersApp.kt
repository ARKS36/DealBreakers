package com.example.appdeal

import android.app.Application
import com.google.firebase.FirebaseApp

class DealBreakersApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize Firebase
        FirebaseApp.initializeApp(this)
    }
} 