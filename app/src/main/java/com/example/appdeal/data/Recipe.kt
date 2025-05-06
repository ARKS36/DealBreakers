package com.example.appdeal.data

data class Recipe(
    val id: String,
    val name: String,
    val description: String,
    val ingredients: List<Ingredient>,
    val instructions: List<String>,
    val prepTime: Int, // in minutes
    val cookTime: Int, // in minutes
    val servings: Int,
    val category: String,
    val tags: List<String> = listOf(),
    val imageUrl: String? = null
)

data class Ingredient(
    val name: String,
    val amount: Double,
    val unit: String,
    val notes: String? = null
) 