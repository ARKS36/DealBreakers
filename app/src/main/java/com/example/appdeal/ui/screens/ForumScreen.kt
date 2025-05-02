// Updated ForumScreen.kt to fix Likes, Favorites, Comments, and Card Styling
package com.example.appdeal.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appdeal.data.DatabaseInstance
import com.example.appdeal.data.UserDeal
import com.example.appdeal.ui.viewmodel.ProductViewModel
import com.example.appdeal.ui.viewmodel.ProductViewModelFactory
import com.example.appdeal.ui.viewmodel.UserDealViewModel
import com.example.appdeal.ui.viewmodel.UserDealViewModelFactory
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForumScreen() {
    val context = LocalContext.current
    val dao = DatabaseInstance.getDatabase(context).userDealDao()
    val userDealViewModel: UserDealViewModel = viewModel(factory = UserDealViewModelFactory(dao))
    val productViewModel: ProductViewModel = viewModel(factory = ProductViewModelFactory())
    val allDeals by userDealViewModel.allDeals.collectAsState(initial = emptyList())

    var showDialog by remember { mutableStateOf(false) }
    var commentDialogDeal by remember { mutableStateOf<UserDeal?>(null) }

    val comments = remember { mutableStateMapOf<Int, MutableList<String>>() }
    val likes = remember { mutableStateMapOf<Int, Boolean>() }
    val favorites = remember { mutableStateMapOf<Int, Boolean>() }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Deal")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                text = "Forum Deals",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(allDeals) { deal ->
                    ForumPostItem(
                        deal = deal,
                        isLiked = likes.getOrDefault(deal.itemName.hashCode(), false),
                        isFavorited = favorites.getOrDefault(deal.itemName.hashCode(), false),
                        comments = comments.getOrDefault(deal.itemName.hashCode(), mutableListOf()),
                        onLikeClick = {
                            likes[deal.itemName.hashCode()] = !(likes[deal.itemName.hashCode()] ?: false)
                        },
                        onFavoriteClick = {
                            val current = favorites.getOrDefault(deal.itemName.hashCode(), false)
                            favorites[deal.itemName.hashCode()] = !current
                            if (!current) {
                                productViewModel.addFavoriteDeal(deal)
                            } else {
                                productViewModel.removeFavoriteDeal(deal)
                            }
                        },
                        onCommentClick = {
                            commentDialogDeal = deal
                        }
                    )
                }
            }
        }

        if (showDialog) {
            DealCreationDialog(
                onDismiss = { showDialog = false },
                onSubmit = { name, store, priceValue ->
                    userDealViewModel.insertDeal(
                        UserDeal(
                            itemName = name,
                            storeName = store,
                            price = priceValue,
                            quantity = null,
                            unit = null
                        )
                    )
                    showDialog = false
                }
            )
        }

        commentDialogDeal?.let { deal ->
            CommentDialog(
                deal = deal,
                existingComments = comments.getOrPut(deal.itemName.hashCode()) { mutableListOf() },
                onDismiss = { commentDialogDeal = null }
            )
        }
    }
}

@Composable
fun ForumPostItem(
    deal: UserDeal,
    isLiked: Boolean,
    isFavorited: Boolean,
    comments: List<String>,
    onLikeClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onCommentClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("${deal.itemName} at ${deal.storeName}", style = MaterialTheme.typography.titleMedium)
            Text("$${deal.price}", style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = onLikeClick) {
                    Icon(
                        Icons.Filled.ThumbUp,
                        contentDescription = "Like",
                        tint = if (isLiked) Color.Blue else Color.Black
                    )
                }
                IconButton(onClick = onCommentClick) {
                    Icon(Icons.Filled.Comment, contentDescription = "Comment")
                }
                IconButton(onClick = onFavoriteClick) {
                    Icon(
                        Icons.Filled.Favorite,
                        contentDescription = "Favorite",
                        tint = if (isFavorited) Color.Red else Color.Black
                    )
                }
            }

            if (comments.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Comments:", style = MaterialTheme.typography.titleSmall)
                comments.forEach { comment ->
                    Text("- $comment", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
