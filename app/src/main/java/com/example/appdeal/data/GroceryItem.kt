package com.example.appdeal.data

data class GroceryItem(
    val id: String,
    val name: String,
    val category: String,
    val price: Double,
    val quantity: String,
    val unit: String,
    val imageUrl: String = "",
    val isOnSale: Boolean = false,
    val regularPrice: Double? = null,
    val tags: List<String> = emptyList()
) 