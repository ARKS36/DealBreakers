package com.example.appdeal.data

class RecipeRepository {
    private val recipes = mutableListOf<Recipe>()

    fun searchRecipesByIngredients(availableIngredients: List<String>): List<Recipe> {
        return recipes.filter { recipe ->
            // Check if all ingredients in the recipe are available
            recipe.ingredients.all { ingredient ->
                availableIngredients.any { available ->
                    available.contains(ingredient.name, ignoreCase = true)
                }
            }
        }
    }

    fun searchRecipes(query: String): List<Recipe> {
        return recipes.filter { recipe ->
            recipe.name.contains(query, ignoreCase = true) ||
            recipe.description.contains(query, ignoreCase = true) ||
            recipe.category.contains(query, ignoreCase = true) ||
            recipe.tags.any { it.contains(query, ignoreCase = true) }
        }
    }

    fun getRecipesByCategory(category: String): List<Recipe> {
        return recipes.filter { it.category == category }
    }

    // For demo purposes, adding sample data
    init {
        recipes.addAll(listOf(
            Recipe(
                id = "1",
                name = "Scrambled Eggs",
                description = "Classic fluffy scrambled eggs",
                ingredients = listOf(
                    Ingredient("Eggs", 4.0, "count"),
                    Ingredient("Milk", 0.25, "cup"),
                    Ingredient("Butter", 1.0, "tbsp"),
                    Ingredient("Salt", 0.25, "tsp"),
                    Ingredient("Pepper", 0.125, "tsp")
                ),
                instructions = listOf(
                    "Whisk eggs, milk, salt, and pepper in a bowl",
                    "Melt butter in a non-stick pan over medium heat",
                    "Pour in egg mixture and stir gently until eggs are set",
                    "Serve immediately"
                ),
                prepTime = 5,
                cookTime = 5,
                servings = 2,
                category = "Breakfast",
                tags = listOf("quick", "easy", "protein")
            ),
            Recipe(
                id = "2",
                name = "Grilled Cheese Sandwich",
                description = "Classic grilled cheese sandwich with a crispy exterior and gooey interior",
                ingredients = listOf(
                    Ingredient("Bread", 2.0, "slices"),
                    Ingredient("Cheese", 2.0, "slices"),
                    Ingredient("Butter", 1.0, "tbsp")
                ),
                instructions = listOf(
                    "Butter one side of each bread slice",
                    "Place cheese between bread slices with buttered sides facing out",
                    "Cook in a pan over medium heat until golden brown on both sides",
                    "Serve hot"
                ),
                prepTime = 5,
                cookTime = 10,
                servings = 1,
                category = "Lunch",
                tags = listOf("quick", "vegetarian")
            ),
            Recipe(
                id = "3",
                name = "Chicken Stir Fry",
                description = "Quick and healthy chicken stir fry with vegetables",
                ingredients = listOf(
                    Ingredient("Chicken Breast", 1.0, "lb"),
                    Ingredient("Bell Peppers", 2.0, "count"),
                    Ingredient("Broccoli", 1.0, "cup"),
                    Ingredient("Soy Sauce", 2.0, "tbsp"),
                    Ingredient("Garlic", 2.0, "cloves"),
                    Ingredient("Ginger", 1.0, "tbsp"),
                    Ingredient("Vegetable Oil", 2.0, "tbsp")
                ),
                instructions = listOf(
                    "Cut chicken and vegetables into bite-sized pieces",
                    "Heat oil in a wok or large pan",
                    "Add garlic and ginger, stir for 30 seconds",
                    "Add chicken and cook until no longer pink",
                    "Add vegetables and stir fry until tender-crisp",
                    "Add soy sauce and stir to combine",
                    "Serve hot over rice if desired"
                ),
                prepTime = 15,
                cookTime = 15,
                servings = 4,
                category = "Dinner",
                tags = listOf("healthy", "protein", "quick")
            )
        ))
    }
} 