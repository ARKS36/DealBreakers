package com.example.appdeal

import org.w3c.dom.Text
import java.lang.reflect.Modifier

@androidx.compose.runtime.Composable

fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}