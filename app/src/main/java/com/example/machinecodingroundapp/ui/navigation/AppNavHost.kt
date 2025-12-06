package com.example.machinecodingroundapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.machinecodingroundapp.ui.SplashScreen
import com.example.machinecodingroundapp.ui.home.HomeScreen
import com.example.machinecodingroundapp.ui.home.VideoViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.Splash.route,
        modifier = modifier
    ) {

        composable(NavRoutes.Splash.route) {
            SplashScreen(
                onNavigateHome = {
                    navController.navigate(NavRoutes.Home.route) {
                        popUpTo(NavRoutes.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(NavRoutes.Home.route) {
            val vm: VideoViewModel = hiltViewModel()
            HomeScreen(vm)
        }
    }
}
