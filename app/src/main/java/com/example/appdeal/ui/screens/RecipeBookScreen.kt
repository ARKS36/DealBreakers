package com.example.appdeal.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.appdeal.data.Recipe
import com.example.appdeal.data.RecipeRepository
import com.example.appdeal.ui.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeBookScreen(viewModel: ProductViewModel) {
    var ingredientInput by remember { mutableStateOf("") }
    var availableIngredients by remember { mutableStateOf(listOf<String>()) }
    var matchingRecipes by remember { mutableStateOf(listOf<Recipe>()) }
    var selectedRecipe by remember { mutableStateOf<Recipe?>(null) }
    
    val recipeRepository = remember { RecipeRepository() }
    val favoritedProducts = viewModel.getFavoritedProducts()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = if (selectedRecipe == null) "Recipe Book" else "Recipe Details") },
                navigationIcon = {
                    if (selectedRecipe != null) {
                        IconButton(onClick = { selectedRecipe = null }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { 
                        if (ingredientInput.isNotEmpty()) {
                            viewModel.toggleFavoriteIngredient(ingredientInput.trim())
                            ingredientInput = ""
                        }
                    }) {
                        Icon(Icons.Default.Favorite, contentDescription = "Favorite Ingredients")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (selectedRecipe != null) {
                // Display recipe details
                RecipeDetailScreen(recipe = selectedRecipe!!, favoritedProducts = favoritedProducts, viewModel = viewModel)
            } else {
                // Main recipe book view
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Ingredient input
                    OutlinedTextField(
                        value = ingredientInput,
                        onValueChange = { ingredientInput = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Enter ingredient") },
                        singleLine = true,
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    if (ingredientInput.isNotEmpty()) {
                                        availableIngredients = availableIngredients + ingredientInput
                                        ingredientInput = ""
                                    }
                                }
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Add Ingredient")
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Available ingredients list
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Available Ingredients",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            if (availableIngredients.isEmpty()) {
                                Text(
                                    text = "No ingredients added yet",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            } else {
                                Column {
                                    availableIngredients.forEachIndexed { index, ingredient ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = ingredient,
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                            
                                            Row {
                                                IconButton(
                                                    onClick = { viewModel.toggleFavoriteIngredient(ingredient) }
                                                ) {
                                                    Icon(
                                                        imageVector = if (viewModel.isIngredientFavorite(ingredient)) 
                                                            Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                                        contentDescription = "Favorite Ingredient",
                                                        tint = if (viewModel.isIngredientFavorite(ingredient)) 
                                                            MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                                    )
                                                }
                                                
                                                IconButton(
                                                    onClick = {
                                                        availableIngredients = availableIngredients.toMutableList().apply {
                                                            removeAt(index)
                                                        }
                                                    }
                                                ) {
                                                    Icon(
                                                        Icons.Default.Clear,
                                                        contentDescription = "Remove Ingredient",
                                                        tint = MaterialTheme.colorScheme.error
                                                    )
                                                }
                                            }
                                        }
                                        
                                        if (index < availableIngredients.lastIndex) {
                                            Divider(modifier = Modifier.padding(vertical = 4.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Find matching recipes button
                    Button(
                        onClick = {
                            if (availableIngredients.isNotEmpty()) {
                                matchingRecipes = recipeRepository.searchRecipesByIngredients(availableIngredients)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = availableIngredients.isNotEmpty()
                    ) {
                        Icon(Icons.Default.Search, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Find Matching Recipes")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Matching recipes section
                    Text(
                        text = "Matching Recipes",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    if (matchingRecipes.isEmpty()) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No matching recipes found",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(matchingRecipes) { recipe ->
                                RecipeCard(
                                    recipe = recipe,
                                    favoritedProducts = favoritedProducts,
                                    onClick = { selectedRecipe = recipe },
                                    isFavorite = viewModel.isFavoriteRecipe(recipe),
                                    onFavoriteClick = { 
                                        if (viewModel.isFavoriteRecipe(recipe)) {
                                            viewModel.removeFavoriteRecipe(recipe)
                                        } else {
                                            viewModel.addFavoriteRecipe(recipe) 
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
} 