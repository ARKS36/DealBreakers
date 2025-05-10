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

    fun getProductsByIds(ids: List<String>): List<Product> {
        return products.filter { product -> ids.contains(product.id) }
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
            ),

            // Fruits & Vegetables
            Product(
                id = "7",
                name = "Organic Bananas",
                marketPrices = mapOf(
                    "Whole Foods" to 0.89,
                    "ACME" to 0.79,
                    "Giant" to 0.69,
                    "ShopRite" to 0.75,
                    "Armanch" to 0.79
                ),
                quantity = "1",
                unit = "lb",
                category = "Produce",
                tags = listOf("organic", "fruit", "potassium"),
                nutritionInfo = NutritionInfo(
                    servingSize = "1 medium",
                    calories = 105,
                    protein = 1.3,
                    carbohydrates = 27.0,
                    fat = 0.4,
                    fiber = 3.1,
                    sugar = 14.0
                ),
                averageRating = 4.7f,
                numberOfReviews = 132
            ),
            
            Product(
                id = "8",
                name = "Avocado",
                marketPrices = mapOf(
                    "Whole Foods" to 2.49,
                    "ACME" to 1.99,
                    "Giant" to 1.89,
                    "ShopRite" to 2.29,
                    "Armanch" to 1.79
                ),
                quantity = "1",
                unit = "each",
                category = "Produce",
                tags = listOf("fruit", "healthy-fat", "guacamole"),
                nutritionInfo = NutritionInfo(
                    servingSize = "1/2 avocado",
                    calories = 160,
                    protein = 2.0,
                    carbohydrates = 8.5,
                    fat = 14.7,
                    fiber = 6.7,
                    sugar = 0.7
                ),
                averageRating = 4.8f,
                numberOfReviews = 178,
                isOnSale = true,
                regularPrice = 2.49
            ),
            
            // Pantry Items
            Product(
                id = "9",
                name = "Olive Oil",
                marketPrices = mapOf(
                    "Whole Foods" to 12.99,
                    "ACME" to 10.99,
                    "Giant" to 11.49,
                    "ShopRite" to 9.99,
                    "Armanch" to 11.99
                ),
                quantity = "16.9",
                unit = "fl oz",
                category = "Pantry",
                tags = listOf("oil", "cooking", "mediterranean"),
                nutritionInfo = NutritionInfo(
                    servingSize = "1 tbsp",
                    calories = 120,
                    protein = 0.0,
                    carbohydrates = 0.0,
                    fat = 14.0,
                    fiber = 0.0,
                    sugar = 0.0
                ),
                averageRating = 4.5f,
                numberOfReviews = 89
            ),
            
            Product(
                id = "10",
                name = "Peanut Butter",
                marketPrices = mapOf(
                    "Whole Foods" to 4.99,
                    "ACME" to 3.99,
                    "Giant" to 3.49,
                    "ShopRite" to 3.79,
                    "Armanch" to 3.89
                ),
                quantity = "16",
                unit = "oz",
                category = "Pantry",
                tags = listOf("spread", "protein", "sandwich"),
                nutritionInfo = NutritionInfo(
                    servingSize = "2 tbsp",
                    calories = 190,
                    protein = 7.0,
                    carbohydrates = 7.0,
                    fat = 16.0,
                    fiber = 2.0,
                    sugar = 3.0
                ),
                averageRating = 4.6f,
                numberOfReviews = 126
            ),
            
            // Frozen Foods
            Product(
                id = "11",
                name = "Frozen Pizza",
                marketPrices = mapOf(
                    "Whole Foods" to 6.99,
                    "ACME" to 5.99,
                    "Giant" to 5.49,
                    "ShopRite" to 5.79,
                    "Armanch" to 5.89
                ),
                quantity = "12",
                unit = "inch",
                category = "Frozen",
                tags = listOf("pizza", "dinner", "quick"),
                nutritionInfo = NutritionInfo(
                    servingSize = "1/6 pizza",
                    calories = 290,
                    protein = 12.0,
                    carbohydrates = 39.0,
                    fat = 10.0,
                    fiber = 2.0,
                    sugar = 4.0
                ),
                averageRating = 4.2f,
                numberOfReviews = 105,
                isOnSale = true,
                regularPrice = 6.99
            ),
            
            Product(
                id = "12",
                name = "Ice Cream",
                marketPrices = mapOf(
                    "Whole Foods" to 5.49,
                    "ACME" to 4.99,
                    "Giant" to 4.49,
                    "ShopRite" to 4.79,
                    "Armanch" to 4.89
                ),
                quantity = "1",
                unit = "pint",
                category = "Frozen",
                tags = listOf("dessert", "sweet", "dairy"),
                nutritionInfo = NutritionInfo(
                    servingSize = "1/2 cup",
                    calories = 250,
                    protein = 3.0,
                    carbohydrates = 30.0,
                    fat = 13.0,
                    fiber = 0.0,
                    sugar = 25.0
                ),
                averageRating = 4.8f,
                numberOfReviews = 185
            ),
            
            // Baking Goods
            Product(
                id = "13",
                name = "All-Purpose Flour",
                marketPrices = mapOf(
                    "Whole Foods" to 3.99,
                    "ACME" to 2.99,
                    "Giant" to 2.49,
                    "ShopRite" to 2.79,
                    "Armanch" to 2.89
                ),
                quantity = "5",
                unit = "lb",
                category = "Baking",
                tags = listOf("baking", "essentials", "pantry"),
                nutritionInfo = NutritionInfo(
                    servingSize = "1/4 cup",
                    calories = 110,
                    protein = 3.0,
                    carbohydrates = 23.0,
                    fat = 0.0,
                    fiber = 1.0,
                    sugar = 0.0
                ),
                averageRating = 4.4f,
                numberOfReviews = 67
            ),
            
            Product(
                id = "14",
                name = "Granulated Sugar",
                marketPrices = mapOf(
                    "Whole Foods" to 3.49,
                    "ACME" to 2.79,
                    "Giant" to 2.29,
                    "ShopRite" to 2.59,
                    "Armanch" to 2.69
                ),
                quantity = "4",
                unit = "lb",
                category = "Baking",
                tags = listOf("baking", "sweetener", "pantry"),
                nutritionInfo = NutritionInfo(
                    servingSize = "1 tsp",
                    calories = 15,
                    protein = 0.0,
                    carbohydrates = 4.0,
                    fat = 0.0,
                    fiber = 0.0,
                    sugar = 4.0
                ),
                averageRating = 4.3f,
                numberOfReviews = 55
            ),
            
            // Canned Goods
            Product(
                id = "15",
                name = "Canned Tuna",
                marketPrices = mapOf(
                    "Whole Foods" to 2.49,
                    "ACME" to 1.99,
                    "Giant" to 1.79,
                    "ShopRite" to 1.89,
                    "Armanch" to 1.95
                ),
                quantity = "5",
                unit = "oz",
                category = "Canned Goods",
                tags = listOf("seafood", "protein", "pantry"),
                nutritionInfo = NutritionInfo(
                    servingSize = "1/4 cup",
                    calories = 70,
                    protein = 16.0,
                    carbohydrates = 0.0,
                    fat = 1.0,
                    fiber = 0.0,
                    sugar = 0.0
                ),
                averageRating = 4.2f,
                numberOfReviews = 92
            ),
            
            Product(
                id = "16",
                name = "Canned Beans",
                marketPrices = mapOf(
                    "Whole Foods" to 1.49,
                    "ACME" to 0.99,
                    "Giant" to 0.89,
                    "ShopRite" to 0.95,
                    "Armanch" to 1.09
                ),
                quantity = "15",
                unit = "oz",
                category = "Canned Goods",
                tags = listOf("beans", "protein", "vegetarian"),
                nutritionInfo = NutritionInfo(
                    servingSize = "1/2 cup",
                    calories = 110,
                    protein = 7.0,
                    carbohydrates = 20.0,
                    fat = 0.0,
                    fiber = 7.0,
                    sugar = 1.0
                ),
                averageRating = 4.0f,
                numberOfReviews = 78,
                isOnSale = true,
                regularPrice = 1.29
            )
        ))
    }
} 