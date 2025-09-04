package com.example.machinecodingroundapp.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.machinecodingroundapp.viewmodels.PokemonListViewModel

@Composable
fun PokemonDetailsScreen(
    pokemonId: String,
    modifier: Modifier = Modifier,
) {

    Box(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Column {
            Text("This is details screen : $pokemonId")
        }
    }
}