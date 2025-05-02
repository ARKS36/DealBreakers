package com.example.appdeal.data

import com.example.appdeal.data.Product
import com.example.appdeal.data.UserDeal

sealed class FavoriteItem {
    data class FavoriteProduct(val product: Product) : FavoriteItem()
    data class FavoriteDeal(val deal: UserDeal) : FavoriteItem()
}
