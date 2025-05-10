package com.example.appdeal.data

object SampleRecipes {
    val recipes = listOf(
        Recipe(
            id = "1",
            title = "Classic Pasta with Tomato Sauce",
            description = "Simple and delicious pasta with homemade tomato sauce",
            imageUrl = "",
            prepTime = 10,
            cookTime = 20,
            servings = 4,
            category = "Italian",
            tags = listOf("pasta", "easy", "vegetarian", "dinner"),
            ingredients = listOf(
                "Pasta",
                "Tomatoes",
                "Garlic",
                "Olive Oil",
                "Basil",
                "Salt",
                "Pepper",
                "Parmesan Cheese"
            ),
            instructions = listOf(
                "Boil water and cook pasta according to package instructions.",
                "Heat olive oil in a pan and add minced garlic. Sauté until fragrant.",
                "Add chopped tomatoes and cook for 10 minutes.",
                "Season with salt, pepper, and chopped basil.",
                "Drain pasta and mix with sauce.",
                "Serve with grated parmesan cheese."
            ),
            relatedProductIds = listOf("g7", "g6")
        ),
        Recipe(
            id = "2",
            title = "Vegetable Stir Fry",
            description = "Quick and healthy vegetable stir fry with rice",
            imageUrl = "",
            prepTime = 15,
            cookTime = 10,
            servings = 2,
            category = "Asian",
            tags = listOf("stir-fry", "quick", "vegetarian", "healthy"),
            ingredients = listOf(
                "Rice",
                "Bell Peppers",
                "Carrots",
                "Broccoli",
                "Soy Sauce",
                "Garlic",
                "Ginger",
                "Vegetable Oil"
            ),
            instructions = listOf(
                "Cook rice according to package instructions.",
                "Heat oil in a wok or large pan.",
                "Add minced garlic and ginger, stir for 30 seconds.",
                "Add vegetables and stir fry for 5-7 minutes until tender-crisp.",
                "Add soy sauce and stir to combine.",
                "Serve over cooked rice."
            ),
            relatedProductIds = listOf("g8", "g10")
        ),
        Recipe(
            id = "3",
            title = "Classic Grilled Cheese",
            description = "Simple and comforting grilled cheese sandwich",
            imageUrl = "",
            prepTime = 5,
            cookTime = 5,
            servings = 1,
            category = "American",
            tags = listOf("sandwich", "quick", "comfort food", "lunch"),
            ingredients = listOf(
                "Bread",
                "Cheddar Cheese",
                "Butter"
            ),
            instructions = listOf(
                "Butter one side of each bread slice.",
                "Place cheese between bread slices with buttered sides facing out.",
                "Cook in a pan over medium heat until golden brown on both sides and cheese is melted.",
                "Serve hot."
            ),
            relatedProductIds = listOf("g3", "g9")
        ),
        Recipe(
            id = "4",
            title = "Scrambled Eggs",
            description = "Fluffy and delicious scrambled eggs",
            imageUrl = "",
            prepTime = 5,
            cookTime = 5,
            servings = 2,
            category = "Breakfast",
            tags = listOf("eggs", "quick", "protein", "breakfast"),
            ingredients = listOf(
                "Eggs",
                "Milk",
                "Salt",
                "Pepper",
                "Butter"
            ),
            instructions = listOf(
                "Crack eggs into a bowl and whisk with a splash of milk, salt, and pepper.",
                "Heat butter in a non-stick pan over medium-low heat.",
                "Add egg mixture and gently stir with a spatula as the eggs cook.",
                "Cook until eggs are just set but still slightly moist.",
                "Serve immediately."
            ),
            relatedProductIds = listOf("g2", "g1")
        ),
        Recipe(
            id = "5",
            title = "Baked Chicken with Vegetables",
            description = "One-pan baked chicken breast with seasonal vegetables",
            imageUrl = "",
            prepTime = 15,
            cookTime = 30,
            servings = 4,
            category = "Dinner",
            tags = listOf("chicken", "healthy", "one-pan", "dinner"),
            ingredients = listOf(
                "Chicken Breast",
                "Carrots",
                "Potatoes",
                "Broccoli",
                "Olive Oil",
                "Garlic",
                "Thyme",
                "Salt",
                "Pepper"
            ),
            instructions = listOf(
                "Preheat oven to 375°F (190°C).",
                "Place chicken breasts in the center of a baking dish.",
                "Arrange chopped vegetables around the chicken.",
                "Drizzle with olive oil and sprinkle with minced garlic, thyme, salt, and pepper.",
                "Bake for 25-30 minutes until chicken is cooked through and vegetables are tender.",
                "Serve hot."
            ),
            relatedProductIds = listOf("g4", "g10")
        )
    )
} 