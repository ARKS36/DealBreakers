package com.example.appdeal.data

data class Recipe(
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: String,
    val prepTime: Int,
    val cookTime: Int,
    val servings: Int,
    val category: String,
    val tags: List<String>,
    val ingredients: List<String>,
    val instructions: List<String>,
    val relatedProductIds: List<String> = emptyList()
)

data class Ingredient(
    val name: String,
    val amount: Double,
    val unit: String,
    val notes: String? = null
) 