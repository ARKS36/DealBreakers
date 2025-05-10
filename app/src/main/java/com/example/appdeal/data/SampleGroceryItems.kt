package com.example.appdeal.data

object SampleGroceryItems {
    val groceryItems = listOf(
        GroceryItem(
            id = "g1",
            name = "Milk",
            category = "Dairy",
            price = 3.99,
            quantity = "1",
            unit = "Gallon",
            isOnSale = true,
            regularPrice = 4.99,
            tags = listOf("dairy", "breakfast", "calcium")
        ),
        GroceryItem(
            id = "g2",
            name = "Eggs",
            category = "Protein",
            price = 4.29,
            quantity = "12",
            unit = "Count",
            tags = listOf("protein", "breakfast", "baking")
        ),
        GroceryItem(
            id = "g3",
            name = "Bread",
            category = "Bakery",
            price = 2.99,
            quantity = "1",
            unit = "Loaf",
            tags = listOf("bakery", "sandwich", "breakfast")
        ),
        GroceryItem(
            id = "g4",
            name = "Chicken Breast",
            category = "Meat & Poultry",
            price = 6.99,
            quantity = "1",
            unit = "lb",
            tags = listOf("protein", "meat", "dinner")
        ),
        GroceryItem(
            id = "g5",
            name = "Apples",
            category = "Produce",
            price = 1.99,
            quantity = "1",
            unit = "lb",
            isOnSale = true,
            regularPrice = 2.49,
            tags = listOf("fruit", "snack", "healthy")
        ),
        GroceryItem(
            id = "g6",
            name = "Tomatoes",
            category = "Produce",
            price = 2.49,
            quantity = "1",
            unit = "lb",
            tags = listOf("vegetable", "salad", "cooking")
        ),
        GroceryItem(
            id = "g7",
            name = "Pasta",
            category = "Pantry",
            price = 1.79,
            quantity = "16",
            unit = "oz",
            tags = listOf("pantry", "dinner", "italian")
        ),
        GroceryItem(
            id = "g8",
            name = "Rice",
            category = "Pantry",
            price = 4.99,
            quantity = "5",
            unit = "lb",
            tags = listOf("pantry", "dinner", "side dish")
        ),
        GroceryItem(
            id = "g9",
            name = "Cheddar Cheese",
            category = "Dairy",
            price = 3.49,
            quantity = "8",
            unit = "oz",
            tags = listOf("dairy", "sandwich", "snack")
        ),
        GroceryItem(
            id = "g10",
            name = "Spinach",
            category = "Produce",
            price = 2.99,
            quantity = "10",
            unit = "oz",
            tags = listOf("vegetable", "salad", "healthy")
        )
    )
} 