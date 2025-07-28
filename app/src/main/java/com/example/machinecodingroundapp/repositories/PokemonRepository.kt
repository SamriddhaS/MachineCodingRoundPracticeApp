package com.example.machinecodingroundapp.repositories
import com.example.machinecodingroundapp.data.models.PokemonListItem

interface PokemonRepository {
    suspend fun getPokemonList(noOfPokemon:Int,pageNo:Int): List<PokemonListItem>
}