package com.example.appdeal.data

class RecipeRepository {
    private val recipes = mutableListOf<Recipe>()

    fun searchRecipesByIngredients(availableIngredients: List<String>): List<Recipe> {
        if (availableIngredients.isEmpty()) {
            return emptyList()
        }
        
        return recipes.filter { recipe ->
            // Check if any ingredients in the recipe match any available ingredients
            availableIngredients.any { available ->
                recipe.ingredients.any { ingredient ->
                    ingredient.contains(available, ignoreCase = true) || 
                    available.contains(ingredient, ignoreCase = true)
                }
            }
        }
    }

    fun searchRecipes(query: String): List<Recipe> {
        return recipes.filter { recipe ->
            recipe.title.contains(query, ignoreCase = true) ||
            recipe.description.contains(query, ignoreCase = true) ||
            recipe.category.contains(query, ignoreCase = true) ||
            recipe.tags.any { it.contains(query, ignoreCase = true) }
        }
    }

    fun getRecipesByCategory(category: String): List<Recipe> {
        return recipes.filter { it.category == category }
    }

    // For demonstration purposes
    fun addRecipe(recipe: Recipe) {
        recipes.add(recipe)
    }

    // Initialize with sample data
    init {
        // Load recipes from SampleRecipes to ensure consistency
        val sampleRecipes = SampleRecipes.recipes
        recipes.clear()
        recipes.addAll(sampleRecipes)
    }
} 