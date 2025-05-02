package com.example.catsapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.catsapp.feature.cats.navigation.CatsRoute
import com.example.catsapp.feature.cats.navigation.catsScreen
import com.example.catsapp.feature.cats.navigation.navigateToCatDetail

@Composable
fun MainNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = CatsRoute
    ) {
        catsScreen(
            navigateToCatDetail = navController::navigateToCatDetail,
            navigateBack = navController::popBackStack
        )
    }
} 