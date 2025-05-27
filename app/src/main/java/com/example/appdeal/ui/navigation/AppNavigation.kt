package com.example.appdeal.ui.navigation

import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.appdeal.RecipeBookActivity
import com.example.appdeal.navigation.Screen
import com.example.appdeal.ui.screens.*
import com.example.appdeal.ui.viewmodel.ProductViewModel
import com.example.appdeal.ui.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(
    viewModel: ProductViewModel,
    userViewModel: UserViewModel,
    paddingValues: PaddingValues = PaddingValues()
) {
    val navController = rememberNavController()
    
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPaddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier
                .padding(paddingValues)  // Apply the outer padding first
                .padding(innerPaddingValues)  // Then apply the inner padding from the bottom bar
        ) {
            composable(Screen.Home.route) {
                HomeScreen(userViewModel = userViewModel)
            }
            composable(Screen.Search.route) {
                SearchScreen(viewModel = viewModel)
            }
            composable(Screen.Favorites.route) {
                FavoritesScreen(viewModel = viewModel)
            }
            composable(Screen.RecipeBook.route) {
                RecipeBookScreen(viewModel = viewModel)
            }
            composable(Screen.Profile.route) {
                ProfileScreen(productViewModel = viewModel, userViewModel = userViewModel)
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        val items = listOf(
            NavigationItem(Screen.Home, "Home", Icons.Default.Home),
            NavigationItem(Screen.Search, "Search", Icons.Default.Search),
            NavigationItem(Screen.Favorites, "Favorites", Icons.Default.Favorite),
            NavigationItem(Screen.RecipeBook, "Recipes", Icons.Default.MenuBook),
            NavigationItem(Screen.Profile, "Profile", Icons.Default.Person)
        )

        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = currentRoute == item.screen.route,
                onClick = {
                    if (currentRoute != item.screen.route) {
                        navController.navigate(item.screen.route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                }
            )
        }
    }
}

private data class NavigationItem(
    val screen: Screen,
    val title: String,
    val icon: ImageVector
) 