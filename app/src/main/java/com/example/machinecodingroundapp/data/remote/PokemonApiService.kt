package com.example.machinecodingroundapp.data.remote


import com.example.machinecodingroundapp.data.models.PokemonListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PokemonApiService {
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") noOfPokemon:Int,
        @Query("offset") pageNo:Int
    ):PokemonListResponse


}