package com.example.machinecodingroundapp.repositories
import com.example.machinecodingroundapp.data.models.PokemonListItem
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    suspend fun loadPokemonList(noOfPokemon:Int,pageNo:Int)
    suspend fun observePokemonList(): Flow<List<PokemonListItem>>
}