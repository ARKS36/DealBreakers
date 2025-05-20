package com.example.appdeal.ui.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appdeal.LoginActivity
import com.example.appdeal.data.FavoriteItem
import com.example.appdeal.data.UserDeal
import com.example.appdeal.ui.viewmodel.ProductViewModel
import com.example.appdeal.ui.viewmodel.UserDealViewModel
import com.example.appdeal.ui.viewmodel.UserViewModel
import com.example.appdeal.ui.viewmodel.UserStats
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    productViewModel: ProductViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel()
) {
    val context = LocalContext.current
    val favoriteItems by productViewModel.favoriteItems.collectAsState()
    val favoriteRecipes by productViewModel.favoriteRecipes.collectAsState()
    val favoriteIngredients by productViewModel.favoriteIngredients.collectAsState()
    
    val currentUser by userViewModel.currentUser.collectAsState()
    val isAuthenticated by userViewModel.isAuthenticated.collectAsState()
    val userStats by userViewModel.userStats.collectAsState()
    val profileImageUrl by userViewModel.profileImageUrl.collectAsState()
    
    val username = currentUser?.name ?: "Guest"
    
    val tabs = listOf("Profile", "Favorites", "Activity")
    var selectedTabIndex by remember { mutableStateOf(0) }
    
    Column(modifier = Modifier.fillMaxSize()) {
        // Profile Header
        ProfileHeader(
            username = username,
            profileImageUrl = profileImageUrl,
            isAuthenticated = isAuthenticated
        )
        
        // Stats bar when authenticated
        if (isAuthenticated) {
            UserStatsBar(userStats = userStats)
        }
        
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
            0 -> ProfileInfo(
                username = username,
                isAuthenticated = isAuthenticated, 
                email = currentUser?.email,
                createdAt = currentUser?.createdAt,
                userViewModel = userViewModel
            )
            1 -> FavoritesSection(
                favoriteItems = favoriteItems,
                favoriteRecipes = favoriteRecipes,
                favoriteIngredients = favoriteIngredients
            )
            2 -> ActivitySection(favoriteItems = favoriteItems)
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Login/Logout Button
        if (isAuthenticated) {
            Button(
                onClick = {
                    userViewModel.logout()
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
        } else {
            Button(
                onClick = {
                    val intent = Intent(context, LoginActivity::class.java)
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Login,
                    contentDescription = "Login"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Login")
            }
        }
    }
}

@Composable
fun ProfileHeader(
    username: String,
    profileImageUrl: String? = null,
    isAuthenticated: Boolean = false
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile Image - clickable to change profile picture if authenticated
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                .clickable(enabled = isAuthenticated) {
                    // This would launch image picker in a real implementation
                },
            contentAlignment = Alignment.Center
        ) {
            // Show icon if no profile image is set
            if (profileImageUrl == null) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile Picture",
                    modifier = Modifier.size(50.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            } else {
                // In a real app, we would load the image from the URL
                // For now, just showing a camera icon to indicate it's a photo
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Profile Picture",
                    modifier = Modifier.size(50.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            
            // Show edit badge if authenticated
            if (isAuthenticated) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .border(1.dp, MaterialTheme.colorScheme.primary, CircleShape)
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Profile Picture",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Username
        Text(
            text = username,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        
        Text(
            text = if (!isAuthenticated) "Sign in to access all features"
                   else "Active Member",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun UserStatsBar(userStats: UserStats) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatItem(
                value = userStats.savedDeals.toString(),
                label = "Saved Deals",
                icon = Icons.Default.Bookmark
            )
            
            VerticalDivider(
                modifier = Modifier
                    .height(48.dp)
                    .width(1.dp)
            )
            
            StatItem(
                value = userStats.contributedDeals.toString(),
                label = "Contributed",
                icon = Icons.Default.Add
            )
            
            VerticalDivider(
                modifier = Modifier
                    .height(48.dp)
                    .width(1.dp)
            )
            
            StatItem(
                value = "$${String.format("%.2f", userStats.totalSavings)}",
                label = "Saved",
                icon = Icons.Default.AttachMoney
            )
        }
    }
}

@Composable
fun StatItem(
    value: String, 
    label: String, 
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun ProfileInfo(
    username: String,
    isAuthenticated: Boolean = false,
    email: String? = null,
    createdAt: Long? = null,
    userViewModel: UserViewModel
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        item {
            // Account Details Card
            ProfileInfoCard(
                title = "Account Details",
                items = listOf(
                    "Username: $username",
                    "Email: ${if (isAuthenticated && email != null) email else "Not available"}",
                    "Member since: ${if (isAuthenticated && createdAt != null) formatDate(createdAt) else "Not available"}"
                )
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Settings Card
            var notificationsEnabled by remember { mutableStateOf(true) }
            var darkModeEnabled by remember { mutableStateOf(false) }
            var locationEnabled by remember { mutableStateOf(true) }
            
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
                        text = "App Settings",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    
                    // Notifications Toggle
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Notifications",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                        
                        Switch(
                            checked = notificationsEnabled,
                            onCheckedChange = { notificationsEnabled = it },
                            enabled = isAuthenticated
                        )
                    }
                    
                    Divider()
                    
                    // Dark Mode Toggle
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.DarkMode,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Dark Mode",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                        
                        Switch(
                            checked = darkModeEnabled,
                            onCheckedChange = { darkModeEnabled = it }
                        )
                    }
                    
                    Divider()
                    
                    // Location Toggle
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Location Services",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                        
                        Switch(
                            checked = locationEnabled,
                            onCheckedChange = { locationEnabled = it },
                            enabled = isAuthenticated
                        )
                    }
                }
            }
            
            // Additional options for authenticated users
            if (isAuthenticated) {
                Spacer(modifier = Modifier.height(16.dp))
                
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
                            text = "Account Actions",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        
                        ActionButton(
                            text = "Edit Profile",
                            icon = Icons.Default.Edit,
                            onClick = { /* Navigate to edit profile screen */ }
                        )
                        
                        Divider()
                        
                        var showChangePasswordDialog by remember { mutableStateOf(false) }
                        
                        ActionButton(
                            text = "Change Password",
                            icon = Icons.Default.Lock,
                            onClick = { showChangePasswordDialog = true }
                        )
                        
                        if (showChangePasswordDialog) {
                            AlertDialog(
                                onDismissRequest = { showChangePasswordDialog = false },
                                title = { },
                                text = { 
                                    ChangePasswordScreen(
                                        userViewModel = userViewModel,
                                        onPasswordChanged = { showChangePasswordDialog = false },
                                        onCancel = { showChangePasswordDialog = false }
                                    )
                                },
                                confirmButton = { },
                                properties = DialogProperties(
                                    dismissOnBackPress = true,
                                    dismissOnClickOutside = false,
                                    usePlatformDefaultWidth = false
                                ),
                                modifier = Modifier
                                    .fillMaxWidth(0.95f)
                                    .heightIn(max = 600.dp)
                            )
                        }
                        
                        Divider()
                        
                        ActionButton(
                            text = "Privacy Settings",
                            icon = Icons.Default.Security,
                            onClick = { /* Navigate to privacy settings */ }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ActionButton(
    text: String, 
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge
        )
        
        Spacer(modifier = Modifier.weight(1f))
        
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
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
                        title = recipe.title,
                        subtitle = "${recipe.ingredients.size} ingredients • ${recipe.prepTime} mins"
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
                                subtitle = "Price: $${item.product.cheapestPrice}"
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
                            "Product from ${item.product.cheapestMarket}",
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