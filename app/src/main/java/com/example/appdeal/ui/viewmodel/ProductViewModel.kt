package com.example.appdeal.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appdeal.data.Product
import com.example.appdeal.data.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.update
import com.example.appdeal.data.FavoriteItem
import com.example.appdeal.data.UserDeal
import com.example.appdeal.data.Recipe


class ProductViewModel : ViewModel() {
    private val repository = ProductRepository()
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    private val _searchResults = MutableStateFlow<List<Product>>(emptyList())
    val searchResults: StateFlow<List<Product>> = _searchResults.asStateFlow()
    
    private val _favoriteProducts = MutableStateFlow<List<Product>>(emptyList())
    val favoriteProducts: StateFlow<List<Product>> = _favoriteProducts.asStateFlow()

    private val _favoriteRecipes = MutableStateFlow<List<Recipe>>(emptyList())
    val favoriteRecipes: StateFlow<List<Recipe>> = _favoriteRecipes.asStateFlow()

    private val _favoriteIngredients = MutableStateFlow<List<String>>(emptyList())
    val favoriteIngredients: StateFlow<List<String>> = _favoriteIngredients.asStateFlow()

    private val _favoriteItems = MutableStateFlow<List<FavoriteItem>>(emptyList())
    val favoriteItems: StateFlow<List<FavoriteItem>> = _favoriteItems

    fun addFavoriteProduct(product: Product) {
        _favoriteItems.value = _favoriteItems.value + FavoriteItem.FavoriteProduct(product)
    }

    fun addFavoriteDeal(deal: UserDeal) {
        _favoriteItems.value = _favoriteItems.value + FavoriteItem.FavoriteDeal(deal)
    }

    fun getFavoritedProducts(): List<String> {
        return _favoriteProducts.value.map { it.name }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        if (query.isNotEmpty()) {
            _searchResults.value = repository.searchProducts(query)
        } else {
            _searchResults.value = emptyList()
        }
    }
    
    fun clearSearch() {
        _searchQuery.value = ""
        _searchResults.value = emptyList()
    }

    fun toggleFavorite(product: Product) {
        _favoriteProducts.update { currentFavorites ->
            if (currentFavorites.any { it.id == product.id }) {
                currentFavorites.filter { it.id != product.id }
            } else {
                currentFavorites + product
            }
        }
        
        // Also update the favoriteItems list to keep it in sync
        if (isFavorite(product)) {
            _favoriteItems.update { it.filterNot { 
                (it is FavoriteItem.FavoriteProduct) && it.product.id == product.id 
            } }
        } else {
            addFavoriteProduct(product)
        }
    }

    fun isFavorite(product: Product): Boolean {
        return _favoriteProducts.value.any { it.id == product.id }
    }

    fun addFavoriteRecipe(recipe: Recipe) {
        _favoriteRecipes.value = _favoriteRecipes.value + recipe
    }

    fun removeFavoriteRecipe(recipe: Recipe) {
        _favoriteRecipes.value = _favoriteRecipes.value.filter { it.id != recipe.id }
    }

    fun isFavoriteRecipe(recipe: Recipe): Boolean {
        return _favoriteRecipes.value.any { it.id == recipe.id }
    }

    fun getProductsByIds(ids: List<String>): List<Product> {
        return repository.getProductsByIds(ids)
    }

    fun addFavoriteIngredient(ingredient: String) {
        if (!_favoriteIngredients.value.contains(ingredient)) {
            _favoriteIngredients.value = _favoriteIngredients.value + ingredient
        }
    }

    fun removeFavoriteIngredient(ingredient: String) {
        _favoriteIngredients.value = _favoriteIngredients.value.filter { it != ingredient }
    }

    fun isIngredientFavorite(ingredient: String): Boolean {
        return _favoriteIngredients.value.contains(ingredient)
    }

    fun toggleFavoriteIngredient(ingredient: String) {
        if (isIngredientFavorite(ingredient)) {
            removeFavoriteIngredient(ingredient)
        } else {
            addFavoriteIngredient(ingredient)
        }
    }
} 