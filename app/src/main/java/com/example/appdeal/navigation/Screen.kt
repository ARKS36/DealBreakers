package com.example.appdeal.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Search : Screen("search")
    object Forum : Screen("forum")
    object Favorites : Screen("favorites")
    object RecipeBook : Screen("recipebook")
    object Profile : Screen("profile")

    companion object {
        fun bottomNavItems() = listOf(
            Home,
            Search,
            Favorites,
            RecipeBook,
            Profile
        )
    }
} 