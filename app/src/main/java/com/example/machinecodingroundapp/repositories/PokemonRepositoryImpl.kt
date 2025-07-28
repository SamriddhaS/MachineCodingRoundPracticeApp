package com.example.machinecodingroundapp.repositories

import com.example.machinecodingroundapp.data.models.PokemonListItem
import com.example.machinecodingroundapp.data.remote.PokemonApiService
import javax.inject.Inject

class PokemonRepositoryImpl

@Inject
constructor(
    private val pokemonApiService: PokemonApiService
):PokemonRepository {
    override suspend fun getPokemonList(noOfPokemon: Int, pageNo: Int): List<PokemonListItem> {
        try {
            val response = pokemonApiService.getPokemonList(noOfPokemon, pageNo)
            return response.results
        }catch (e:Exception){
            throw e
        }
    }
}