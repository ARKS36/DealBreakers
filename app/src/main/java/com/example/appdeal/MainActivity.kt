package com.example.appdeal

import android.graphics.drawable.Icon
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appdeal.ui.theme.AppdealTheme
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.appdeal.DatabaseHelper






class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppdealTheme {
                Surface (
                    modifier = Modifier.fillMaxSize()
                   ) {
                    BaseScreen()
                }
            }
        }
    }
}

fun showToast(context: android.content.Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

enum class Screen {
    Screen1,
    Screen2,
    Screen3,
    StartScreen,
    Search
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseScreen() {
    var searchText by remember { mutableStateOf("") }
    val screens = listOf("Screen 1", "Screen 2", "Screen 3")
    var currentScreen by remember { mutableStateOf(Screen.StartScreen) }

    val context = androidx.compose.ui.platform.LocalContext.current
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        when (currentScreen) {
            Screen.StartScreen -> Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                Column {
                    TextField(
                        value = searchText,
                        onValueChange = { searchText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        placeholder = { Text("Search") }
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Text(
                        text = "Welcome to Dealbreakers!",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Button(
                        onClick = { currentScreen = Screen.Search },
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Text(text = "Get Started")
                    }
                }


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    screens.forEach { screen ->
                        Button(
                            onClick = {
                                currentScreen = when (screen) {
                                    "Screen 1" -> Screen.Screen1
                                    "Screen 2" -> Screen.Screen2
                                    "Screen 3" -> Screen.Screen3
                                    else -> Screen.StartScreen
                                }
                                showToast(context, "Navigating to $screen")
                            },
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp)
                    ) {
                        Text(text = screen)
                    }
                }
            }
            }
            Screen.Screen1 -> DisplayScreen(screenName = "Screen 1")
            Screen.Screen2 -> DisplayScreen(screenName = "Screen 2")
            Screen.Screen3 -> DisplayScreen(screenName = "Screen 3")
            Screen.Search -> DisplayScreen(screenName = "Search")

        }
    }
}

@Composable
fun DisplayScreen(screenName: String) {
    Text(text = "This is $screenName")
            }
@Preview(showBackground = true)
@Composable
fun BaseScreenPreview() {
    AppdealTheme {
        BaseScreen()
    }
}