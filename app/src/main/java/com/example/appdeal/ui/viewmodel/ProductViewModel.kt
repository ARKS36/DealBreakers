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


class ProductViewModel : ViewModel() {
    private val repository = ProductRepository()
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    private val _searchResults = MutableStateFlow<List<Product>>(emptyList())
    val searchResults: StateFlow<List<Product>> = _searchResults.asStateFlow()
    
    private val _favoriteProducts = MutableStateFlow<List<Product>>(emptyList())
    val favoriteProducts: StateFlow<List<Product>> = _favoriteProducts.asStateFlow()


    private val _favoriteItems = MutableStateFlow<List<FavoriteItem>>(emptyList())
    val favoriteItems: StateFlow<List<FavoriteItem>> = _favoriteItems

    fun addFavoriteProduct(product: Product) {
        _favoriteItems.value = _favoriteItems.value + FavoriteItem.FavoriteProduct(product)
    }

    fun addFavoriteDeal(deal: UserDeal) {
        _favoriteItems.value = _favoriteItems.value + FavoriteItem.FavoriteDeal(deal)
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
    }

    fun isFavorite(product: Product): Boolean {
        return _favoriteProducts.value.any { it.id == product.id }
    }
} 