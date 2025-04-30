package com.example.catsapp.feature.cats

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
internal fun CatsRoute(){
    CatsScreen()
}


@Composable
fun CatsScreen(){
    Greeting("Android")
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}