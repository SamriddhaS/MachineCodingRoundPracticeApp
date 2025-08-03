package com.example.machinecodingroundapp.repositories

import com.example.machinecodingroundapp.data.local.PokemonDao
import com.example.machinecodingroundapp.data.models.PokemonListItem
import com.example.machinecodingroundapp.data.remote.PokemonApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PokemonRepositoryImpl

@Inject
constructor(
    private val pokemonApiService: PokemonApiService,
    private val pokemonDao: PokemonDao
):PokemonRepository {
    override suspend fun loadPokemonList(noOfPokemon: Int, pageNo: Int) {
        try {
            val response = pokemonApiService.getPokemonList(noOfPokemon, pageNo)
            pokemonDao.insertPokemonList(response.results)
        }catch (e:Exception){
            throw e
        }
    }

    override suspend fun observePokemonList(): Flow<List<PokemonListItem>> {
        return  pokemonDao.observePokemonList()
    }
}