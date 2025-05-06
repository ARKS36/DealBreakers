package com.example.appdeal

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.appdeal.data.RecipeRepository

class RecipeBookActivity : AppCompatActivity() {
    private lateinit var recipeRepository: RecipeRepository
    private lateinit var availableIngredientsList: MutableList<String>
    private lateinit var availableIngredientsAdapter: ArrayAdapter<String>
    private lateinit var matchingRecipesAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_book)

        recipeRepository = RecipeRepository()
        availableIngredientsList = mutableListOf()
        
        // Initialize adapters
        availableIngredientsAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            availableIngredientsList
        )
        
        matchingRecipesAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            mutableListOf()
        )

        // Set up views
        val ingredientsListView = findViewById<ListView>(R.id.availableIngredientsListView)
        val recipesListView = findViewById<ListView>(R.id.matchingRecipesListView)
        val ingredientInput = findViewById<EditText>(R.id.ingredientInput)
        val addIngredientButton = findViewById<Button>(R.id.addIngredientButton)
        val findRecipesButton = findViewById<Button>(R.id.findRecipesButton)
        val noIngredientsText = findViewById<TextView>(R.id.noIngredientsText)
        val noRecipesText = findViewById<TextView>(R.id.noRecipesText)

        ingredientsListView.adapter = availableIngredientsAdapter
        recipesListView.adapter = matchingRecipesAdapter

        // Add ingredient button click listener
        addIngredientButton.setOnClickListener {
            val ingredient = ingredientInput.text.toString().trim()
            if (ingredient.isNotEmpty()) {
                availableIngredientsList.add(ingredient)
                availableIngredientsAdapter.notifyDataSetChanged()
                ingredientInput.text.clear()
                updateNoIngredientsVisibility()
            }
        }

        // Find recipes button click listener
        findRecipesButton.setOnClickListener {
            val matchingRecipes = recipeRepository.searchRecipesByIngredients(availableIngredientsList)
            val recipeStrings = matchingRecipes.map { recipe ->
                "${recipe.name}\n" +
                "Prep: ${recipe.prepTime}min | Cook: ${recipe.cookTime}min | Serves: ${recipe.servings}\n" +
                "Ingredients: ${recipe.ingredients.joinToString { it.name }}"
            }
            
            matchingRecipesAdapter.clear()
            matchingRecipesAdapter.addAll(recipeStrings)
            matchingRecipesAdapter.notifyDataSetChanged()
            
            noRecipesText.visibility = if (matchingRecipes.isEmpty()) View.VISIBLE else View.GONE
        }

        // Set up ingredient list item click listener for removal
        ingredientsListView.setOnItemClickListener { _, _, position, _ ->
            availableIngredientsList.removeAt(position)
            availableIngredientsAdapter.notifyDataSetChanged()
            updateNoIngredientsVisibility()
        }

        // Set up recipe list item click listener for details
        recipesListView.setOnItemClickListener { _, _, position, _ ->
            // TODO: Show recipe details
        }

        updateNoIngredientsVisibility()
    }

    private fun updateNoIngredientsVisibility() {
        val noIngredientsText = findViewById<TextView>(R.id.noIngredientsText)
        noIngredientsText.visibility = if (availableIngredientsList.isEmpty()) View.VISIBLE else View.GONE
    }
} 