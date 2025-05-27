package com.example.appdeal.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appdeal.data.FirebaseManager
import com.example.appdeal.data.Recipe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RecipeViewModel : ViewModel() {
    private val firebaseManager = FirebaseManager()
    
    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes: StateFlow<List<Recipe>> = _recipes.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    init {
        loadRecipes()
    }
    
    fun loadRecipes() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            firebaseManager.getRecipes()
                .onSuccess { recipeList ->
                    _recipes.value = recipeList
                }
                .onFailure { error ->
                    _errorMessage.value = "Failed to load recipes: ${error.message}"
                }
            
            _isLoading.value = false
        }
    }
    
    fun saveRecipe(recipe: Recipe) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            firebaseManager.saveRecipe(recipe)
                .onSuccess { _ ->
                    loadRecipes() // Refresh the list
                }
                .onFailure { error ->
                    _errorMessage.value = "Failed to save recipe: ${error.message}"
                    _isLoading.value = false
                }
        }
    }
    
    fun deleteRecipe(recipeId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            firebaseManager.deleteRecipe(recipeId)
                .onSuccess { _ ->
                    loadRecipes() // Refresh the list
                }
                .onFailure { error ->
                    _errorMessage.value = "Failed to delete recipe: ${error.message}"
                    _isLoading.value = false
                }
        }
    }
    
    fun uploadImage(imageBytes: ByteArray, fileName: String, onComplete: (String?) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            
            firebaseManager.uploadImage(imageBytes, fileName)
                .onSuccess { url ->
                    onComplete(url)
                }
                .onFailure { error ->
                    _errorMessage.value = "Failed to upload image: ${error.message}"
                    onComplete(null)
                }
            
            _isLoading.value = false
        }
    }
} 