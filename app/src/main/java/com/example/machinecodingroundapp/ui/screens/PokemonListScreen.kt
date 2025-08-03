package com.example.machinecodingroundapp.ui.screens

import androidx.compose.foundation.layout.Box
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
fun PokemonListScreen(
    modifier: Modifier = Modifier,
    viewModel: PokemonListViewModel = hiltViewModel()
) {

    val pokemonList by viewModel.pokemonList.collectAsState()
    val error by viewModel.isError.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Box(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        if (!error.isNullOrEmpty()) {
            Text("Something went wrong ! ${error}")
        }else{
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                itemsIndexed(pokemonList){ index,pokemon ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(text = "${pokemon.name}")
                    }
                    if (index == pokemonList.lastIndex) {
                        viewModel.loadPokemonWithPagination()
                    }
                }
            }
            if (isLoading){
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(androidx.compose.ui.Alignment.BottomCenter)
                        .padding(16.dp)
                )
            }
        }
    }
}