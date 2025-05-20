package com.example.appdeal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appdeal.ui.navigation.AppNavigation
import com.example.appdeal.ui.theme.AppDealTheme
import com.example.appdeal.ui.viewmodel.ProductViewModel
import com.example.appdeal.ui.viewmodel.UserViewModel

class MainActivity : ComponentActivity() {
    private lateinit var userViewModel: UserViewModel
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Get the user ID from intent extras if available
        val userId = intent.getStringExtra("USER_ID")
        
        setContent {
            AppDealTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val productViewModel: ProductViewModel = viewModel()
                    userViewModel = viewModel()
                    
                    // Check if we got a userId from the login activity
                    if (userId != null) {
                        // This is a simple approach - in a real app, we'd use this ID to load user data
                        // For demo purposes, we'll just update the authentication state
                        userViewModel.setAuthenticated(true)
                    } else {
                        // Try to load user from shared preferences
                        val preferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
                        userViewModel.loadUserFromPreferences(preferences)
                    }
                    
                    AppNavigation(
                        viewModel = productViewModel,
                        userViewModel = userViewModel
                    )
                }
            }
        }
    }
    
    override fun onStop() {
        super.onStop()
        
        // Save user state in preferences
        val preferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        userViewModel.saveUserToPreferences(preferences)
    }
}