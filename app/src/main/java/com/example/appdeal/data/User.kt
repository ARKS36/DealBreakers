package com.example.appdeal.data

data class User(
    val id: String,
    val email: String,
    val password: String, // In a real app, this should be hashed
    val name: String? = null,
    val createdAt: Long = System.currentTimeMillis()
) 