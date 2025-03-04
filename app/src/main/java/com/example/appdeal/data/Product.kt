package com.example.appdeal.data

data class Product(
    val id: String,
    val name: String,
    val marketPrices: Map<String, Double>, // Market name to price mapping
    val quantity: String,
    val unit: String,
    val category: String = "", // e.g., "Dairy", "Meat", "Produce"
    val tags: List<String> = listOf(), // e.g., "organic", "gluten-free"
    val nutritionInfo: NutritionInfo? = null,
    val averageRating: Float = 0f,
    val numberOfReviews: Int = 0,
    val isOnSale: Boolean = false,
    val regularPrice: Double? = null // Original price before sale
) {
    val cheapestPrice: Double
        get() = marketPrices.values.minOrNull() ?: 0.0

    val cheapestMarket: String
        get() = marketPrices.entries.minByOrNull { it.value }?.key ?: ""
        
    val savingsAmount: Double
        get() = regularPrice?.let { it - cheapestPrice } ?: 0.0
        
    val savingsPercentage: Int
        get() = regularPrice?.let { ((it - cheapestPrice) / it * 100).toInt() } ?: 0
}

data class NutritionInfo(
    val servingSize: String,
    val calories: Int,
    val protein: Double,
    val carbohydrates: Double,
    val fat: Double,
    val fiber: Double,
    val sugar: Double
) 