package com.example.appdeal.data

class ProductRepository {
    // In a real app, this would be fetched from a database or API
    private val products = mutableListOf<Product>()

    fun searchProducts(query: String): List<Product> {
        return products.filter { product ->
            product.name.contains(query, ignoreCase = true) ||
            product.category.contains(query, ignoreCase = true) ||
            product.tags.any { it.contains(query, ignoreCase = true) }
        }
    }

    fun getProductsByCategory(category: String): List<Product> {
        return products.filter { it.category == category }
    }

    fun getOnSaleProducts(): List<Product> {
        return products.filter { it.isOnSale }
    }

    fun getTopRatedProducts(): List<Product> {
        return products.filter { it.numberOfReviews > 0 }
            .sortedByDescending { it.averageRating }
    }

    // For demo purposes, adding sample data
    init {
        // Sample data
        products.addAll(listOf(
            // Dairy Products
            Product(
                id = "1",
                name = "2% Milk",
                marketPrices = mapOf(
                    "Whole Foods" to 4.99,
                    "ACME" to 4.49,
                    "Giant" to 4.29,
                    "ShopRite" to 3.99,
                    "Armanch" to 4.19
                ),
                quantity = "1",
                unit = "Gallon",
                category = "Dairy",
                tags = listOf("vitamin D", "calcium-rich"),
                nutritionInfo = NutritionInfo(
                    servingSize = "1 cup",
                    calories = 120,
                    protein = 8.0,
                    carbohydrates = 12.0,
                    fat = 5.0,
                    fiber = 0.0,
                    sugar = 12.0
                ),
                averageRating = 4.5f,
                numberOfReviews = 128,
                isOnSale = true,
                regularPrice = 4.99
            ),

            // Bakery Products
            Product(
                id = "2",
                name = "White Bread",
                marketPrices = mapOf(
                    "Whole Foods" to 3.99,
                    "ACME" to 2.99,
                    "Giant" to 3.29,
                    "ShopRite" to 3.49,
                    "Armanch" to 2.89
                ),
                quantity = "1",
                unit = "Loaf",
                category = "Bakery",
                tags = listOf("fresh-baked", "sandwich-bread"),
                nutritionInfo = NutritionInfo(
                    servingSize = "1 slice",
                    calories = 110,
                    protein = 3.0,
                    carbohydrates = 21.0,
                    fat = 1.0,
                    fiber = 1.0,
                    sugar = 1.5
                ),
                averageRating = 4.3f,
                numberOfReviews = 95
            ),

            // Protein & Eggs
            Product(
                id = "3",
                name = "Large Eggs",
                marketPrices = mapOf(
                    "Whole Foods" to 4.99,
                    "ACME" to 4.49,
                    "Giant" to 4.29,
                    "ShopRite" to 4.19,
                    "Armanch" to 3.99
                ),
                quantity = "12",
                unit = "Count",
                category = "Protein",
                tags = listOf("fresh", "grade A"),
                nutritionInfo = NutritionInfo(
                    servingSize = "1 egg",
                    calories = 70,
                    protein = 6.0,
                    carbohydrates = 0.0,
                    fat = 5.0,
                    fiber = 0.0,
                    sugar = 0.0
                ),
                averageRating = 4.7f,
                numberOfReviews = 203,
                isOnSale = true,
                regularPrice = 5.49
            ),

            // Snacks
            Product(
                id = "4",
                name = "Potato Chips",
                marketPrices = mapOf(
                    "Whole Foods" to 4.49,
                    "ACME" to 3.99,
                    "Giant" to 3.79,
                    "ShopRite" to 3.49,
                    "Armanch" to 3.89
                ),
                quantity = "8",
                unit = "oz",
                category = "Snacks",
                tags = listOf("classic", "family-size"),
                nutritionInfo = NutritionInfo(
                    servingSize = "1 oz",
                    calories = 150,
                    protein = 2.0,
                    carbohydrates = 15.0,
                    fat = 10.0,
                    fiber = 1.0,
                    sugar = 0.0
                ),
                averageRating = 4.4f,
                numberOfReviews = 167
            ),

            // Meat & Poultry
            Product(
                id = "5",
                name = "Chicken Breast",
                marketPrices = mapOf(
                    "Whole Foods" to 7.99,
                    "ACME" to 7.49,
                    "Giant" to 6.79,
                    "ShopRite" to 6.99,
                    "Armanch" to 7.29
                ),
                quantity = "1",
                unit = "lb",
                category = "Meat & Poultry",
                tags = listOf("fresh", "skinless", "boneless"),
                nutritionInfo = NutritionInfo(
                    servingSize = "4 oz",
                    calories = 120,
                    protein = 26.0,
                    carbohydrates = 0.0,
                    fat = 1.5,
                    fiber = 0.0,
                    sugar = 0.0
                ),
                averageRating = 4.6f,
                numberOfReviews = 189,
                isOnSale = true,
                regularPrice = 8.99
            ),

            // Beverages
            Product(
                id = "6",
                name = "Cola Soda",
                marketPrices = mapOf(
                    "Whole Foods" to 6.99,
                    "ACME" to 6.49,
                    "Giant" to 6.29,
                    "ShopRite" to 5.89,
                    "Armanch" to 6.19
                ),
                quantity = "12",
                unit = "Pack",
                category = "Beverages",
                tags = listOf("soft-drink", "carbonated", "classic"),
                nutritionInfo = NutritionInfo(
                    servingSize = "12 oz",
                    calories = 140,
                    protein = 0.0,
                    carbohydrates = 39.0,
                    fat = 0.0,
                    fiber = 0.0,
                    sugar = 39.0
                ),
                averageRating = 4.4f,
                numberOfReviews = 156
            )
        ))
    }
} 