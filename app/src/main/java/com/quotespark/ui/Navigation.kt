package com.quotespark.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel // Added this for hiltViewModel()
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.quotespark.ui.screens.FavoritesScreen
import com.quotespark.ui.screens.HomeScreen
import com.quotespark.viewmodel.QuoteViewModel // Added this for QuoteViewModel

object Routes {
    const val HOME = "home"
    const val FAVORITES = "favorites"
}

@Composable
fun QuoteSparkNavGraph(
    navController: NavHostController = rememberNavController()
) {
    // Corrected spelling to hiltViewModel() and added explicit type tracking
    val sharedViewModel: QuoteViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = Routes.HOME) {
        composable(Routes.HOME) {
            HomeScreen(
                onNavigateToFavorites = { navController.navigate(Routes.FAVORITES) },
                viewModel = sharedViewModel
            )
        }
        composable(Routes.FAVORITES) {
            FavoritesScreen(
                onNavigateBack = { navController.popBackStack() },
                viewModel = sharedViewModel
            )
        }
    }
}