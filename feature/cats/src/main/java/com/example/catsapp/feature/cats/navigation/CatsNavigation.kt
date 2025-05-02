package com.example.catsapp.feature.cats.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.example.catsapp.feature.cats.CatsRoute
import com.example.catsapp.feature.cats.detail.CatDetailRoute
import kotlinx.serialization.Serializable

@Serializable
data object CatsRoute

@Serializable
data class CatDetailRoute(val catId: String)

fun NavController.navigateToCats(navOptions: NavOptions) =
    navigate(route = CatsRoute, navOptions)

fun NavController.navigateToCatDetail(
    catId: String,
    navOptions: NavOptionsBuilder.() -> Unit = {},
) =
    navigate(route = CatDetailRoute(catId), navOptions)

fun NavGraphBuilder.catsScreen(
    navigateToCatDetail: (String) -> Unit,
    navigateBack: () -> Unit,
) {
    composable<CatsRoute> {
        CatsRoute(
            navigateToCatDetail = navigateToCatDetail,
        )
    }
    composable<CatDetailRoute> {
        CatDetailRoute(
            navigateBack = navigateBack,
        )
    }
}