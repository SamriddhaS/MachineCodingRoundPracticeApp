package com.example.machinecodingroundapp.ui.screens

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink

sealed class AppRoutes(val route:String){
    object PokemonListScreen: AppRoutes("home")
    object PokemonDetailsScreen: AppRoutes("details")
}

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier
) {
    NavHost(navController = navController, startDestination = AppRoutes.PokemonListScreen.route){
        composable(route = AppRoutes.PokemonListScreen.route) {
            PokemonListScreen(modifier = modifier)
        }
        composable(
            route = AppRoutes.PokemonDetailsScreen.route,
            deepLinks = listOf(navDeepLink {
                uriPattern = "https://pokemonapp.dev/details/{pokemonId}?pokemonName={pokemonName}"
                action = Intent.ACTION_VIEW
            }),
            arguments = listOf(
                navArgument("pokemonId"){
                    type = NavType.StringType
                    defaultValue = ""
                },
                navArgument("pokemonName"){
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = true
                }
            )
        ) { backStackEntry ->
            val pokemonId = backStackEntry.arguments?.getString("pokemonId")?:""
            val pokemonName = backStackEntry.arguments?.getString("pokemonName")?:""
            PokemonDetailsScreen(pokemonId = pokemonId+pokemonName, modifier = modifier)
        }
    }
}