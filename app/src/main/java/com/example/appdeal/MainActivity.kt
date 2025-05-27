package com.example.appdeal

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appdeal.ui.navigation.AppNavigation
import com.example.appdeal.ui.theme.AppDealTheme
import com.example.appdeal.ui.viewmodel.ProductViewModel
import com.example.appdeal.ui.viewmodel.UserViewModel
import com.example.appdeal.viewmodels.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    private lateinit var userViewModel: UserViewModel
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            AppDealTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val productViewModel: ProductViewModel = viewModel()
                    userViewModel = viewModel()
                    val authViewModel: AuthViewModel = viewModel()
                    val context = LocalContext.current
                    
                    // Check Firebase authentication state
                    val currentUser by authViewModel.currentUser.collectAsState()
                    val displayName by authViewModel.displayName.collectAsState()
                    
                    if (currentUser == null) {
                        // Redirect to login if not authenticated
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                        return@Surface
                    }
                    
                    // User is authenticated, update user info
                    userViewModel.setAuthenticated(true)
                    currentUser?.let { user ->
                        // Set display name from Firebase user if available
                        displayName?.let { name ->
                            userViewModel.setUserName(name)
                        } ?: user.email?.let { email ->
                            // If no display name, use email as fallback
                            val username = email.substringBefore('@')
                            userViewModel.setUserName(username)
                        }
                        
                        // Set email
                        user.email?.let { email ->
                            userViewModel.setUserEmail(email)
                        }
                    }
                    
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = { Text("DealBreakers") },
                                actions = {
                                    // Logout button in app bar
                                    IconButton(onClick = {
                                        authViewModel.signOut(context)
                                        startActivity(Intent(context, LoginActivity::class.java))
                                        finish()
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Logout,
                                            contentDescription = "Logout"
                                        )
                                    }
                                }
                            )
                        }
                    ) { paddingValues ->
                        AppNavigation(
                            viewModel = productViewModel,
                            userViewModel = userViewModel,
                            paddingValues = paddingValues
                        )
                    }
                }
            }
        }
    }
    
    // We don't need to save user preferences anymore since Firebase handles that
}