package com.example.catsapp.feature.cats.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.catsapp.feature.cats.CatsRoute
import kotlinx.serialization.Serializable

@Serializable
data object CatsRoute

fun NavController.navigateToCats(navOptions: NavOptions) =
    navigate(route = CatsRoute, navOptions)

fun NavGraphBuilder.catsScreen() {
    composable<CatsRoute> {
        CatsRoute()
    }
}