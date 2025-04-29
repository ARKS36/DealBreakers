
package com.example.appdeal.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.appdeal.data.FavoriteItem
import com.example.appdeal.ui.viewmodel.ProductViewModel

@Composable
fun FavoritesScreen(viewModel: ProductViewModel) {
    val favoriteItems by viewModel.favoriteItems.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Your Favorites",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (favoriteItems.isEmpty()) {
            EmptyFavorites()
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(favoriteItems, key = { it.hashCode() }) { favoriteItem ->
                when (favoriteItem) {
                        is FavoriteItem.FavoriteProduct -> {
                            ProductCard(product = favoriteItem.product, viewModel = viewModel)
                        }
                        is FavoriteItem.FavoriteDeal -> {
                            DealCard(deal = favoriteItem.deal)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DealCard(deal: com.example.appdeal.data.UserDeal) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("${deal.itemName} at ${deal.storeName}", style = MaterialTheme.typography.titleMedium)
            Text("\$${deal.price}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun EmptyFavorites() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No favorites yet",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Start adding products to your favorites by tapping the heart icon",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
