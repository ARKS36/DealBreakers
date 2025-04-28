package com.example.appdeal.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appdeal.data.DatabaseInstance
import com.example.appdeal.data.UserDeal
import com.example.appdeal.ui.viewmodel.UserDealViewModel
import com.example.appdeal.ui.viewmodel.UserDealViewModelFactory
import androidx.compose.ui.platform.LocalContext


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForumScreen() {
    val context = LocalContext.current
    val dao = DatabaseInstance.getDatabase(context).userDealDao()
    val viewModel: UserDealViewModel = viewModel(factory = UserDealViewModelFactory(dao))
    val allDeals by viewModel.allDeals.collectAsState(initial = emptyList())

    var showDialog by remember { mutableStateOf(false) }
    var itemName by remember { mutableStateOf(TextFieldValue("")) }
    var storeName by remember { mutableStateOf(TextFieldValue("")) }
    var price by remember { mutableStateOf(TextFieldValue("")) }

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

            LazyColumn {
                items(allDeals) { deal ->
                    Text(
                        text = "${deal.itemName} at ${deal.storeName} - \$${deal.price}",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    Button(
                        onClick = {
                            val priceDouble = price.text.toDoubleOrNull()
                            if (itemName.text.isNotBlank() && storeName.text.isNotBlank() && priceDouble != null) {
                                val newDeal = UserDeal(
                                    itemName = itemName.text,
                                    storeName = storeName.text,
                                    price = priceDouble,
                                    quantity = null,
                                    unit = null
                                )
                                viewModel.insertDeal(newDeal)
                                showDialog = false
                                itemName = TextFieldValue("")
                                storeName = TextFieldValue("")
                                price = TextFieldValue("")
                            }
                        }
                    ) {
                        Text("Submit")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDialog = false }) {
                        Text("Cancel")
                    }
                },
                title = { Text("Add a New Deal") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = itemName,
                            onValueChange = { itemName = it },
                            label = { Text("Item Name") }
                        )
                        OutlinedTextField(
                            value = storeName,
                            onValueChange = { storeName = it },
                            label = { Text("Store Name") }
                        )
                        OutlinedTextField(
                            value = price,
                            onValueChange = { price = it },
                            label = { Text("Price") }
                        )
                    }
                }
            )
        }
    }
}
