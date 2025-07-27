package com.example.machinecodingroundapp.repository

import com.example.machinecodingroundapp.data.model.PokemonListResponse
import com.example.machinecodingroundapp.util.Resource

interface PokemonRepository {
    suspend fun getPokemonList(limit: Int, offset: Int): Resource<PokemonListResponse>
}
