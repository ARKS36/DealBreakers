package com.example.appdeal.ui.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appdeal.LoginActivity
import com.example.appdeal.data.FavoriteItem
import com.example.appdeal.data.UserDeal
import com.example.appdeal.ui.viewmodel.ProductViewModel
import com.example.appdeal.ui.viewmodel.UserDealViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    username: String = "Guest",
    productViewModel: ProductViewModel = viewModel()
) {
    val context = LocalContext.current
    val favoriteItems by productViewModel.favoriteItems.collectAsState()
    val favoriteRecipes by productViewModel.favoriteRecipes.collectAsState()
    val favoriteIngredients by productViewModel.favoriteIngredients.collectAsState()
    
    val tabs = listOf("Profile", "Favorites", "Activity")
    var selectedTabIndex by remember { mutableStateOf(0) }
    
    Column(modifier = Modifier.fillMaxSize()) {
        // Profile Header
        ProfileHeader(username = username)
        
        // Tab Row
        TabRow(selectedTabIndex = selectedTabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title) }
                )
            }
        }
        
        // Tab Content
        when (selectedTabIndex) {
            0 -> ProfileInfo(username = username)
            1 -> FavoritesSection(
                favoriteItems = favoriteItems,
                favoriteRecipes = favoriteRecipes,
                favoriteIngredients = favoriteIngredients
            )
            2 -> ActivitySection(favoriteItems = favoriteItems)
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Logout Button
        Button(
            onClick = { 
                // Navigate to login screen
                val intent = Intent(context, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                context.startActivity(intent)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Logout,
                contentDescription = "Logout"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Logout")
        }
    }
}

@Composable
fun ProfileHeader(username: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile Image
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Profile Picture",
                modifier = Modifier.size(50.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Username
        Text(
            text = username,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        
        Text(
            text = if (username == "Guest") "Sign in to access all features" else "Active Member",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun ProfileInfo(username: String) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        item {
            ProfileInfoCard(
                title = "Account Details",
                items = listOf(
                    "Username: $username",
                    "Email: ${if (username == "Guest") "Not available" else "dealbreakers@gmail.com"}",
                    "Member since: ${if (username == "Guest") "Not available" else formatDate(System.currentTimeMillis() - 30L * 24 * 60 * 60 * 1000)}"
                )
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            ProfileInfoCard(
                title = "App Settings",
                items = listOf(
                    "Notifications: Enabled",
                    "Dark Mode: System default",
                    "Location: Enabled"
                )
            )
        }
    }
}

@Composable
fun ProfileInfoCard(title: String, items: List<String>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            items.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                
                if (item != items.last()) {
                    Divider()
                }
            }
        }
    }
}

@Composable
fun FavoritesSection(
    favoriteItems: List<FavoriteItem>,
    favoriteRecipes: List<com.example.appdeal.data.Recipe>,
    favoriteIngredients: List<String>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Text(
                text = "Your Favorites",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        
        if (favoriteItems.isEmpty() && favoriteRecipes.isEmpty() && favoriteIngredients.isEmpty()) {
            item {
                EmptyStateMessage("You haven't added any favorites yet")
            }
        } else {
            if (favoriteRecipes.isNotEmpty()) {
                item {
                    Text(
                        text = "Recipes (${favoriteRecipes.size})",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                
                items(favoriteRecipes) { recipe ->
                    FavoriteItemRow(
                        title = recipe.name,
                        subtitle = "${recipe.ingredients.size} ingredients • ${recipe.preparationTime} mins"
                    )
                }
            }
            
            if (favoriteItems.isNotEmpty()) {
                item {
                    Text(
                        text = "Products (${favoriteItems.size})",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                
                items(favoriteItems) { item ->
                    when (item) {
                        is FavoriteItem.FavoriteProduct -> {
                            FavoriteItemRow(
                                title = item.product.name,
                                subtitle = "Price: $${item.product.price}"
                            )
                        }
                        is FavoriteItem.FavoriteDeal -> {
                            FavoriteItemRow(
                                title = item.deal.itemName,
                                subtitle = "Store: ${item.deal.storeName} • Price: $${item.deal.price}"
                            )
                        }
                    }
                }
            }
            
            if (favoriteIngredients.isNotEmpty()) {
                item {
                    Text(
                        text = "Ingredients (${favoriteIngredients.size})",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                
                items(favoriteIngredients) { ingredient ->
                    FavoriteItemRow(
                        title = ingredient,
                        subtitle = "Ingredient"
                    )
                }
            }
        }
    }
}

@Composable
fun FavoriteItemRow(title: String, subtitle: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun ActivitySection(favoriteItems: List<FavoriteItem>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Text(
                text = "Recent Activity",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        
        if (favoriteItems.isEmpty()) {
            item {
                EmptyStateMessage("No recent activity to show")
            }
        } else {
            items(favoriteItems.take(10)) { item ->
                val (title, subtitle, timestamp) = when (item) {
                    is FavoriteItem.FavoriteProduct -> {
                        Triple(
                            "Added ${item.product.name} to favorites",
                            "Product from ${item.product.store}",
                            System.currentTimeMillis() - (1..7).random() * 24 * 60 * 60 * 1000
                        )
                    }
                    is FavoriteItem.FavoriteDeal -> {
                        Triple(
                            "Saved deal for ${item.deal.itemName}",
                            "From ${item.deal.storeName} at $${item.deal.price}",
                            item.deal.timestamp
                        )
                    }
                }
                
                ActivityItem(
                    title = title,
                    subtitle = subtitle,
                    timestamp = timestamp
                )
            }
        }
    }
}

@Composable
fun ActivityItem(title: String, subtitle: String, timestamp: Long) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Text(
                    text = formatDate(timestamp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun EmptyStateMessage(message: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = null,
            modifier = Modifier.size(48.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

// Helper function to format date
private fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
} 